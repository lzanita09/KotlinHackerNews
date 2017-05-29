package com.reindeercrafts.hackernews.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface ArticleDao {
    @Query("select * from Article where id = :p0")
    fun findById(id: String): Flowable<Article>

    @Query("select * from Article where id = :p0")
    fun findByIdSync(id: String): Article

    @Query("select * from Article where type = :p0")
    fun findByType(type: String): Flowable<List<Article>>

    @Query("select * from Article where id in (:p0)")
    fun findByIds(ids: Array<String>): Flowable<List<Article>>

    @Query("select * from Article")
    fun findAll(): Flowable<List<Article>>

    @Insert(onConflict = REPLACE)
    fun saveArticle(article: Article)

    @Delete
    fun deleteArticle(article: Article)
}