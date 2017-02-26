package com.reindeercrafts.hackernews.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SourceDbHelper : SQLiteOpenHelper{
    companion object Statics {
        val DB_NAME = "hacker_news"
        val DB_VERSION = 1
        val TEXT_TYPE = " TEXT"
        val BOOLEAN_TYPE = " BOOL"
        val LONG_TYPE = " LONG"
        val COMMA_SEP = ", "
        val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + ArticleContract.TABLE_NAME + " (" +
                ArticleContract._ID + TEXT_TYPE + " PRIMARY KEY," +
                ArticleContract.COLUMN_BY + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_DEAD + BOOLEAN_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_DELETED + BOOLEAN_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_ID + TEXT_TYPE + " UNIQUE " + COMMA_SEP +
                ArticleContract.COLUMN_KIDS + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_PARENT + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_TEXT + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_TIME + LONG_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_SCORE + LONG_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_URL + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
                ArticleContract.COLUMN_TITLE + TEXT_TYPE +
                " )"
    }

    constructor(context: Context) : super(context, DB_NAME, null, DB_VERSION) { }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase!!.execSQL(CREATE_TABLE_SQL)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}