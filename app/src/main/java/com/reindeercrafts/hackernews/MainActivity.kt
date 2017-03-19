package com.reindeercrafts.hackernews

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.reindeercrafts.hackernews.data.ArticleRepository
import com.reindeercrafts.hackernews.data.SharedPrefsHelper
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var sharedPrefHelper: SharedPrefsHelper
    @Inject lateinit var repository: ArticleRepository

    lateinit var controller: ArticleListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as MainApplication).getComponent().inject(this)

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
                        .setPositiveButton(R.string.clear_cache, { _, _ ->
                            repository.trimArticles({
                                controller.update(false)
                            })

                            sharedPrefHelper.clearId()
                        })
                        .setNegativeButton(R.string.cancel, { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        })
                        .show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
