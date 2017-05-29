package com.reindeercrafts.hackernews

import com.reindeercrafts.hackernews.data.Article
import com.reindeercrafts.hackernews.data.ArticleRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommentLoader(private val articleRepository: ArticleRepository) {
    fun loadCommentForArticle(article: Article, callback: (List<Article>) -> Unit) {
        if (article.kids == null) {
            callback.invoke(emptyList())
            return
        }

        Flowable.fromIterable(article.kids)
                .flatMap { loadComment(it) }
                .flatMap { getSubComments(it) }
                .toList().toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback.invoke(it) }
    }

    private fun loadComment(id: String): Flowable<Article> {
        return Flowable.just(id).flatMap {
            articleRepository.getArticle(it)
        }
    }

    private fun getSubComments(article: Article): Flowable<Article> {
        if (article.kids != null) {
            return Flowable.merge(Flowable.just(article),
                    Flowable.fromIterable(article.kids)
                            .flatMap { loadComment(it) }
                            .flatMap { getSubComments(it) })
        }

        return Flowable.just(article)
    }
}