package com.reindeercrafts.hackernews

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.reindeercrafts.hackernews.data.*

class MainActivity : AppCompatActivity() {

    lateinit var repository: ArticleRepository
    lateinit var controller: ArticleListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = ArticleRepository(LocalArticleSource(this),
                RemoteArticleSource(RetrofitHelper.retrofit, SharedPrefsHelper(this)))

        controller = ArticleListController(findViewById(android.R.id.content), repository, {
            startActivity(ArticleActivity.intent(this, it))
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.clear_database -> {
                AlertDialog.Builder(this)
                        .setMessage(R.string.confirm_clear)
                        .setPositiveButton(R.string.clear_cache, { dialogInterface, which ->
                            repository.trimArticles({
                                controller.update(false)
                            })
                        })
                        .setNegativeButton(R.string.cancel, { dialogInterface, which ->
                            dialogInterface.dismiss()
                        })
                        .show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
