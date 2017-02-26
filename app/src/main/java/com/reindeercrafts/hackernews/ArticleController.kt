package com.reindeercrafts.hackernews

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import com.reindeercrafts.hackernews.data.Article

class ArticleController(view: View, private val article: Article, private val callback: (String) -> Unit) {
    private val titleView: TextView = view.findViewById(R.id.title) as TextView
    private val timeAndAuthorView: TextView = view.findViewById(R.id.author) as TextView
    private val contentView: TextView = view.findViewById(R.id.content) as TextView
    private val openButton: Button = view.findViewById(R.id.open_in_browser) as Button

    init {
        titleView.text = article.title
        val timeAndAuthor = SpannableStringBuilder()
        if (article.by != null) {
            timeAndAuthor.append("by ").append(article.by).append(" ")
        }

        timeAndAuthor.append(DateUtils.getRelativeDateTimeString(view.context, article.time * 1000,
                DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0))

        timeAndAuthorView.text = timeAndAuthor

        contentView.text = Html.fromHtml(article.text)

        if (article.url != null) {
            openButton.visibility = VISIBLE
            openButton.setOnClickListener {
                callback.invoke(article.url)
            }
        } else {
            openButton.visibility = GONE
        }
    }
}