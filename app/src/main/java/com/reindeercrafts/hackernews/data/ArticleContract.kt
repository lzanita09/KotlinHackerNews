package com.reindeercrafts.hackernews.data

class ArticleContract private constructor() {
    init {
        throw AssertionError("No instance")
    }

    companion object Columns {
        val TABLE_NAME = "articles"
        val _ID = "_id"
        val COLUMN_ID = "id"
        val COLUMN_DELETED = "deleted"
        val COLUMN_TYPE = "type"
        val COLUMN_BY = "by"
        val COLUMN_TIME = "time"
        val COLUMN_TEXT = "text"
        val COLUMN_DEAD = "dead"
        val COLUMN_PARENT = "parent"
        val COLUMN_KIDS = "kids"
        val COLUMN_URL = "url"
        val COLUMN_SCORE = "score"
        val COLUMN_TITLE = "title"
    }

}