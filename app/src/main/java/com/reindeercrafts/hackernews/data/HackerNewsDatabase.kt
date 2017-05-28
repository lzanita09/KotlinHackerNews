package com.reindeercrafts.hackernews.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = arrayOf(Article::class), version = 1)
@TypeConverters(Article.StringListTypeConverter::class)
abstract class HackerNewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}