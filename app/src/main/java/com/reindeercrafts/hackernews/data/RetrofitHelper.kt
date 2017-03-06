package com.reindeercrafts.hackernews.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object RetrofitInstance {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://hacker-news.firebaseio.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
    }
}