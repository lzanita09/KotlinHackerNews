package com.reindeercrafts.hackernews.data

import android.content.Context

class SharedPrefsHelper(private val context: Context) {
    private val PREFS_NAME = "prefs"
    private val PREFS_KEY_ID = "id"

    fun saveId(id: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREFS_KEY_ID, id).apply()
    }

    fun getId(): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREFS_KEY_ID, null)
    }
}