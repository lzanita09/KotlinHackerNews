package com.reindeercrafts.hackernews.data

import java.util.*

class ArticleRepository(private val localSource: ArticleSource, private val remoteSource: ArticleSource) {

    var cacheDirty = false

    fun setArticleCacheDirty(dirty: Boolean) {
        this.cacheDirty = dirty
    }

    fun getArticles(type: String, sortedByFunction: Comparator<Article>, callback: (List<Article>) -> Unit,
                    remoteCallback: (List<Article>?) -> Unit) {
        localSource.getArticles(type, { articles ->
            if (!articles.isEmpty()) {
                callback.invoke(articles.sortedWith(sortedByFunction))
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

    fun getArticle(id: String, callback: (Article) -> Unit) {
        localSource.getArticle(id, { article ->
            if (article == null) {
                remoteSource.getArticle(id, { remoteArticle ->
                    if (remoteArticle != null) {
                        localSource.saveArticles(listOf(remoteArticle))
                        callback.invoke(remoteArticle)
                    }
                })
            } else {
                callback.invoke(article)
            }
        })
    }

    fun trimArticles(callback: () -> Unit) {
        localSource.getArticles(null, {
            localSource.deleteArticles(it)
            callback.invoke()
        })
    }
}