package com.reindeercrafts.hackernews.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.reindeercrafts.hackernews.data.Article.Companion.Columns
import java.util.*

class LocalArticleSource(context: Context) : ArticleSource {
    val database: SQLiteDatabase = SourceDbHelper(context).writableDatabase

    override fun getArticles(callback: (List<Article>) -> Unit) {
        val cursor = database.query(Columns.TABLE_NAME, null, null, null, null, null, null)
        val articles = ArrayList<Article>(cursor.count)

        while (cursor.moveToNext()) {
            val by = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_BY))
            val dead = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_DEAD)) == 1
            val deleted = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_DELETED)) == 1
            val id = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_ID))
            val kids = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_KIDS))
            val parent = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_PARENT))
            val text = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TEXT))
            val time = cursor.getLong(cursor.getColumnIndex(Columns.COLUMN_TIME))
            val score = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_SCORE))
            val url = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_URL))
            val title = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TITLE))
            val type = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TYPE))

            val kidsList: ArrayList<String>?
            if (kids == null) {
                kidsList = null
            } else {
                kidsList = ArrayList(kids.split(", "))
            }
            val article = Article(id, deleted, type, by, time, text, dead, parent, kidsList, url, score, title)
            articles.add(article)
        }

        cursor.close()
        callback.invoke(articles)
    }

    override fun getArticle(id: String, callback: (Article?) -> Unit) {
        val cursor = database.query(Columns.TABLE_NAME, null, Columns.COLUMN_ID + "=?",
                arrayOf(id), null, null, null, "1")
        if (cursor.moveToNext()) {
            val by = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_BY))
            val dead = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_DEAD)) == 1
            val deleted = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_DELETED)) == 1
            val article_id = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_ID))
            val kids = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_KIDS))
            val parent = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_PARENT))
            val text = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TEXT))
            val time = cursor.getLong(cursor.getColumnIndex(Columns.COLUMN_TIME))
            val score = cursor.getInt(cursor.getColumnIndex(Columns.COLUMN_SCORE))
            val url = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_URL))
            val title = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TITLE))
            val type = cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TYPE))

            callback.invoke(Article(article_id, deleted, type, by, time, text, dead, parent, ArrayList(kids.split(", "))
                    , url, score, title))
        }

        cursor.close()

        callback.invoke(null)
    }

    override fun saveArticles(articles: List<Article>) {
        database.beginTransaction()
        articles.forEach {
            val cv = ContentValues()
            cv.put(Columns.COLUMN_BY, it.by)
            cv.put(Columns.COLUMN_ID, it.id)
            cv.put(Columns.COLUMN_TYPE, it.type)
            cv.put(Columns.COLUMN_TITLE, it.title)
            cv.put(Columns.COLUMN_URL, it.url)
            cv.put(Columns.COLUMN_DELETED, it.deleted)
            cv.put(Columns.COLUMN_SCORE, it.score)
            cv.put(Columns.COLUMN_TIME, it.time)
            cv.put(Columns.COLUMN_DEAD, it.dead)
            cv.put(Columns.COLUMN_PARENT, it.parent)
            cv.put(Columns.COLUMN_TEXT, it.text)
            if (it.kids != null) {
                cv.put(Columns.COLUMN_KIDS, it.kids.joinToString(", "))
            }

            database.insertWithOnConflict(Columns.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        database.setTransactionSuccessful()
        database.endTransaction()
    }

    override fun deleteArticles(articles: List<Article>) {
        database.beginTransaction()
        articles.forEach {
            database.delete(Columns.TABLE_NAME, "id=?", arrayOf(it.id))
        }
        database.setTransactionSuccessful()
        database.endTransaction()
    }

}