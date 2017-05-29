package com.reindeercrafts.hackernews.data

interface ArticleSource {
    fun getArticles(type: String?, callback: (List<Article>) -> Unit)
    fun getArticle(id: String, callback: (Article?) -> Unit)
    fun getArticleSync(id: String): Article?
    fun saveArticles(articles: List<Article>)
    fun deleteArticles(articles: List<Article>)
}