package com.reindeercrafts.hackernews.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.reindeercrafts.hackernews.CommentLoader
import com.reindeercrafts.hackernews.data.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideRoomDatabase(): HackerNewsDatabase {
        return Room.databaseBuilder(application, HackerNewsDatabase::class.java, "hacker_news").build()
    }

    @Provides
    @Singleton
    fun provideArticleRepository(@Named("localSource") localArticleSource: ArticleSource,
                                 @Named("remoteSource") remoteArticleSource: ArticleSource): ArticleRepository {
        return ArticleRepository(localArticleSource, remoteArticleSource)
    }

    @Provides
    @Singleton
    @Named("localSource")
    fun provideLocalArticleSource(hackerNewsDatabase: HackerNewsDatabase): ArticleSource {
        return LocalArticleSource(hackerNewsDatabase.articleDao())
    }

    @Provides
    @Singleton
    @Named("remoteSource")
    fun providRemoteArticleSource(retrofit: Retrofit, sharedPrefsHelper: SharedPrefsHelper): ArticleSource {
        return RemoteArticleSource(retrofit, sharedPrefsHelper)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitHelper.retrofit
    }

    @Provides
    @Singleton
    fun providePrefsHelper(): SharedPrefsHelper {
        return SharedPrefsHelper(application)
    }

    @Provides
    @Singleton
    fun provideCommentLoader(articleRepository: ArticleRepository): CommentLoader {
        return CommentLoader(articleRepository)
    }
}