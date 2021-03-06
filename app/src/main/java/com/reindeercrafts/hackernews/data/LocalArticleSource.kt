package com.reindeercrafts.hackernews.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalArticleSource(private val articleDao: ArticleDao) : ArticleSource {
    override fun getArticleSync(id: String): Article? {
        return articleDao.findByIdSync(id)
    }

    override fun getArticles(type: String?, callback: (List<Article>) -> Unit) {
        if (type != null) {
            articleDao.findByType(type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { callback(it) }

        } else {
            articleDao.findAll().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { callback(it) }
        }
    }

    override fun getArticle(id: String, callback: (Article?) -> Unit) {
        articleDao.findById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback(it) }

    }

    override fun saveArticles(articles: List<Article>) {
        Schedulers.io().scheduleDirect { articles.forEach { articleDao.saveArticle(it) } }
    }

    override fun deleteArticles(articles: List<Article>) {
        Schedulers.io().scheduleDirect { articles.forEach { articleDao.deleteArticle(it) } }
    }
}