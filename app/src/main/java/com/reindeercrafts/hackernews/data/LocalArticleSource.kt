package com.reindeercrafts.hackernews.data

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocalArticleSource(private val articleDao: ArticleDao) : ArticleSource {


    override fun getArticles(type: String?, callback: (List<Article>) -> Unit) {
        if (type != null) {
            articleDao.findByType(type).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { callback.invoke(it) }

        } else {
            articleDao.findAll().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { callback.invoke(it) }
        }
    }

    override fun getArticle(id: String, callback: (Article?) -> Unit) {
        articleDao.findById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback.invoke(it) }

    }

    override fun saveArticles(articles: List<Article>) {
        Schedulers.io().scheduleDirect { articles.forEach { articleDao.saveArticle(it) } }
    }

    override fun deleteArticles(articles: List<Article>) {
        Schedulers.io().scheduleDirect { articles.forEach { articleDao.deleteArticle(it) } }
    }
}