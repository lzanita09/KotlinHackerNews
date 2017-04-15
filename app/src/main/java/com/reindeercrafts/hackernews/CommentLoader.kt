package com.reindeercrafts.hackernews

import android.os.AsyncTask
import com.reindeercrafts.hackernews.data.Article
import com.reindeercrafts.hackernews.data.ArticleApi
import com.reindeercrafts.hackernews.data.RetrofitHelper
import retrofit2.Response
import java.io.IOException
import java.util.*

class CommentLoader {
    private val articleApi = RetrofitHelper.retrofit.create(ArticleApi::class.java)

    fun loadCommentForArticle(article: Article, callback: (List<Article>) -> Unit) {
        CommentAsyncTask(articleApi, article, callback).execute()
    }

    class CommentAsyncTask(private val articleApi: ArticleApi,
                           private val article: Article, private val callback: (List<Article>) -> Unit) :
            AsyncTask<Void, Void, List<Article>>() {

        override fun doInBackground(vararg params: Void?): List<Article> {
            val articles = ArrayList<Article>()
            loadComment(article, articles)
            return articles.filter { !it.text.isNullOrEmpty() }
        }

        override fun onPostExecute(result: List<Article>?) {
            callback.invoke(result!!)
        }

        private fun loadComment(article: Article, articles: ArrayList<Article>) {
            article.kids?.forEach {
                val response: Response<Article>
                try {
                    response = articleApi.getStoryById(it).execute()
                } catch (e: IOException) {
                    return
                }

                if (response.isSuccessful) {
                    val result = response.body()
                    articles.add(result)
                    if (result.kids != null) {
                        loadComment(result, articles)
                    }
                }
            }
        }

    }
}