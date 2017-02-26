package com.reindeercrafts.hackernews.data

class ArticleRepository(private val localSource: ArticleSource, private val remoteSource: ArticleSource) {

    var cacheDirty = false

    fun setArticleCacheDirty(dirty: Boolean) {
        this.cacheDirty = dirty
    }

    fun getArticles(callback: (List<Article>) -> Unit, remoteCallback: (List<Article>?)  -> Unit) {
        localSource.getArticles({ articles ->
            if (!articles.isEmpty()) {
                callback.invoke(articles)
            }

            if (articles.isEmpty() || cacheDirty) {
                remoteSource.getArticles({ remoteArticles ->
                    if (remoteArticles.isNotEmpty()) {
                        localSource.saveArticles(remoteArticles)
                        cacheDirty = false
                    }

                    remoteCallback.invoke(remoteArticles)
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
        localSource.getArticles {
            localSource.deleteArticles(it)
            callback.invoke()
        }
    }
}