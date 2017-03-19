package com.reindeercrafts.hackernews.data

import android.os.AsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class RemoteArticleSource(retrofit: Retrofit, private val prefsHelper: SharedPrefsHelper) : ArticleSource {
    val articleApi: ArticleApi = retrofit.create(ArticleApi::class.java)

    override fun getArticles(type: String?, callback: (List<Article>) -> Unit) {
        articleApi.getTopStoriesIds().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>?, response: Response<List<String>>?) {
                if (!response!!.isSuccessful) {
                    return
                }

                val latestId = prefsHelper.getId()
                val ids = response.body()
                val subList = if (latestId == null) ids.subList(0, 20) else ids.filter { it > latestId }

                subList.sortedByDescending { it }

                FetchArticlesAsyncTask(articleApi, subList, callback).execute()

                if (!subList.isEmpty()) {
                    prefsHelper.saveId(subList[0])
                }
            }

            override fun onFailure(call: Call<List<String>>?, t: Throwable?) {
                t!!.printStackTrace()
            }
        })
    }

    override fun getArticle(id: String, callback: (Article?) -> Unit) {
        articleApi.getStoryById(id).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>?, response: Response<Article>?) {
                if (!response!!.isSuccessful) {
                    return
                }

                callback.invoke(response.body())
            }

            override fun onFailure(call: Call<Article>?, t: Throwable?) {

            }

        })
    }

    override fun saveArticles(articles: List<Article>) {
        // Do nothing.
    }

    class FetchArticlesAsyncTask(val articleApi: ArticleApi, val ids: List<String>,
                                 val callback: (List<Article>) -> Unit) :
            AsyncTask<Void, Void, List<Article>>() {
        override fun doInBackground(vararg p0: Void?): List<Article> {
            val articles: ArrayList<Article> = ArrayList(ids.size)
            ids.forEach {
                val response = articleApi.getStoryById(it).execute()
                if (!response.isSuccessful) {
                    return articles
                }

                articles.add(response.body())
            }

            articles.sortByDescending { it.time }

            return articles
        }

        override fun onPostExecute(result: List<Article>?) {
            callback.invoke(result!!)
        }
    }

    override fun deleteArticles(articles: List<Article>) {
        // Do nothing.
    }
}