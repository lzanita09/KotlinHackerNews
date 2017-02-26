package com.reindeercrafts.hackernews.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.reindeercrafts.hackernews.data.Article.Companion.Columns

class SourceDbHelper : SQLiteOpenHelper {
    companion object Statics {
        val DB_NAME = "hacker_news"
        val DB_VERSION = 1
        val TEXT_TYPE = " TEXT"
        val BOOLEAN_TYPE = " BOOL"
        val LONG_TYPE = " LONG"
        val COMMA_SEP = ", "
        val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + Columns.TABLE_NAME + " (" +
                Columns._ID + TEXT_TYPE + " PRIMARY KEY," +
                Columns.COLUMN_BY + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_DEAD + BOOLEAN_TYPE + COMMA_SEP +
                Columns.COLUMN_DELETED + BOOLEAN_TYPE + COMMA_SEP +
                Columns.COLUMN_ID + TEXT_TYPE + " UNIQUE " + COMMA_SEP +
                Columns.COLUMN_KIDS + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_PARENT + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_TEXT + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_TIME + LONG_TYPE + COMMA_SEP +
                Columns.COLUMN_SCORE + LONG_TYPE + COMMA_SEP +
                Columns.COLUMN_URL + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
                Columns.COLUMN_TITLE + TEXT_TYPE +
                " )"
    }

    constructor(context: Context) : super(context, DB_NAME, null, DB_VERSION) {}

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase!!.execSQL(CREATE_TABLE_SQL)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}