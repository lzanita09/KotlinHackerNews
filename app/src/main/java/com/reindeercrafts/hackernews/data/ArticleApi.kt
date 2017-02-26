package com.reindeercrafts.hackernews.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleApi {
    @GET("/v0/topstories.json?")
    fun getTopStoriesIds(): Call<List<String>>

    @GET("/v0/newstories.json?")
    fun getNewStoriesIds(): Call<List<String>>

    @GET("/v0/beststories.json?")
    fun getBestStoriesIds(): Call<List<String>>

    @GET("/v0/item/{id}.json?")
    fun getStoryById(@Path("id") id: String): Call<Article>
}