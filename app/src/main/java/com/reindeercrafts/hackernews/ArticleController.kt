package com.reindeercrafts.hackernews

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import com.reindeercrafts.hackernews.data.Article

class ArticleController(view: View, private val article: Article, commentLoader: CommentLoader,
                        private val callback: (String) -> Unit) {
    private val titleView: TextView = view.findViewById(R.id.title) as TextView
    private val timeAndAuthorView: TextView = view.findViewById(R.id.author) as TextView
    private val contentView: TextView = view.findViewById(R.id.content) as TextView
    private val commentRecyclerView: RecyclerView = view.findViewById(R.id.comment_recycler) as RecyclerView
    private val loadingView: ProgressBar = view.findViewById(R.id.progress_bar) as ProgressBar

    init {
        titleView.text = article.title
        val timeAndAuthor = SpannableStringBuilder()
        if (article.by != null) {
            timeAndAuthor.append("by ").append(article.by).append(" ")
        }

        timeAndAuthor.append(DateUtils.getRelativeDateTimeString(view.context, article.time * 1000,
                DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0))

        timeAndAuthorView.text = timeAndAuthor

        if (article.text != null) {
            contentView.visibility = VISIBLE
            contentView.text = Html.fromHtml(article.text)
        } else {
            contentView.visibility = GONE
        }

        if (article.url != null) {
            titleView.setOnClickListener {
                callback(article.url)
            }
        }

        val layoutManager = LinearLayoutManager(view.context)
        commentRecyclerView.layoutManager = layoutManager
        commentRecyclerView.addItemDecoration(
                DividerItemDecoration(commentRecyclerView.context, DividerItemDecoration.VERTICAL))
        commentRecyclerView.isNestedScrollingEnabled = false

        loadingView.visibility = VISIBLE
        commentLoader.loadCommentForArticle(article, {
            loadingView.visibility = GONE
            commentRecyclerView.adapter = CommentAdapter(it)
        })
    }

}