package com.reindeercrafts.hackernews

import com.reindeercrafts.hackernews.data.Article
import com.reindeercrafts.hackernews.data.ArticleApi
import com.reindeercrafts.hackernews.data.RetrofitHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommentLoader {
    private val articleApi = RetrofitHelper.retrofit.create(ArticleApi::class.java)

    fun loadCommentForArticle(article: Article, callback: (List<Article>) -> Unit) {
        if (article.kids == null) {
            return
        }

        Observable.fromIterable(article.kids)
                .flatMap { loadComment(it) }
                .flatMap { getSubComments(it) }
                .toList().toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback.invoke(it) }
    }

    private fun loadComment(id: String): Observable<Article> {
        return Observable.just(id).flatMap {
            val response = articleApi.getStoryById(it).execute()
            if (response.isSuccessful && response.body() != null) {
                Observable.just(response.body())
            } else {
                Completable.complete().toObservable()
            }
        }
    }

    private fun getSubComments(article: Article): Observable<Article> {
        if (article.kids != null) {
            return Observable.merge(Observable.just(article),
                    Observable.fromIterable(article.kids)
                            .flatMap { loadComment(it) }
                            .flatMap { getSubComments(it) })
        }

        return Observable.just(article)
    }
}