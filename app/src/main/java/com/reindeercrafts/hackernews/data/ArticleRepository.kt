package com.reindeercrafts.hackernews.data

import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*

class ArticleRepository(private val localSource: ArticleSource, private val remoteSource: ArticleSource) {

    var cacheDirty = false

    fun setArticleCacheDirty(dirty: Boolean) {
        this.cacheDirty = dirty
    }

    fun getArticles(type: String, sortedByFunction: Comparator<Article>, localCallback: (List<Article>) -> Unit,
                    remoteCallback: (List<Article>?) -> Unit) {
        localSource.getArticles(type, { articles ->
            if (!articles.isEmpty()) {
                localCallback.invoke(articles.sortedWith(sortedByFunction))
            }

            if (articles.isEmpty() || cacheDirty) {
                remoteSource.getArticles(type, { remoteArticles ->
                    if (remoteArticles.isNotEmpty()) {
                        localSource.saveArticles(remoteArticles)
                        cacheDirty = false
                        remoteCallback.invoke(remoteArticles.sortedWith(sortedByFunction))
                    }
                })
            }
        })
    }

    fun getArticle(id: String): Flowable<Article> {
        return Flowable.just(id).flatMap {
            val localArticle = localSource.getArticleSync(it)
            if (localArticle != null) {
                // Check local cache to see if it is there already.
                Flowable.just(localArticle)
            } else {
                // Fetch from network and store in database.
                val remoteArticle = remoteSource.getArticleSync(id)
                if (remoteArticle != null) {
                    localSource.saveArticles(listOf(remoteArticle))
                    Flowable.just(remoteArticle)
                } else {
                    Completable.complete().toFlowable()
                }
            }
        }
    }

    fun trimArticles(callback: () -> Unit) {
        localSource.getArticles(null, {
            localSource.deleteArticles(it)
            callback.invoke()
        })
    }
}