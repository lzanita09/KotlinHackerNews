package com.reindeercrafts.hackernews.modules

import android.app.Application
import com.reindeercrafts.hackernews.data.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideArticleRepository(@Named("localSource") localArticleSource: ArticleSource,
                                 @Named("remoteSource") remoteArticleSource: ArticleSource): ArticleRepository {
        return ArticleRepository(localArticleSource, remoteArticleSource)
    }

    @Provides
    @Singleton
    @Named("localSource")
    fun provideLocalArticleSource(): ArticleSource {
        return LocalArticleSource(application)
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

}