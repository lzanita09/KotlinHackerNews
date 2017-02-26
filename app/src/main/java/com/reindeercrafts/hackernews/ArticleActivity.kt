package com.reindeercrafts.hackernews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.reindeercrafts.hackernews.data.Article

class ArticleActivity : AppCompatActivity() {

    companion object IntentFactory {
        private val EXTRA_ARTICLE = "article"
        fun intent(context: Context, article: Article): Intent {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra(EXTRA_ARTICLE, article)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_article)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val article: Article = intent.getParcelableExtra(EXTRA_ARTICLE)
        ArticleController(findViewById(android.R.id.content), article, {
            CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(it))
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}