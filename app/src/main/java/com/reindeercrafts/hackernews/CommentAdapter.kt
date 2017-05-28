package com.reindeercrafts.hackernews

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.reindeercrafts.hackernews.data.Article

class CommentAdapter(private val articles: List<Article>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var previousId: String? = null
    private var previousIndent: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val article = articles[position]
        if (!previousId.isNullOrEmpty() && !article.parent.isNullOrEmpty() && article.parent == previousId) {
            previousIndent += holder.itemView.resources.getDimensionPixelSize(R.dimen.generic_padding_margin_med)
            holder.itemView.setPadding(holder.itemView.paddingLeft + previousIndent,
                    holder.itemView.paddingTop, holder.itemView.paddingRight, holder.itemView.paddingBottom)
        } else {
            previousIndent = 0
        }

        previousId = article.id
        holder.contentView.text =
                if (article.text != null) Html.fromHtml(article.text) else null
        holder.contentView.movementMethod = LinkMovementMethod.getInstance()
        val metaData = article.by + " " + DateUtils.getRelativeDateTimeString(holder.itemView.context,
                article.time * 1000,
                DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
        holder.metaDataView.text = metaData
    }


    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentView: TextView = itemView.findViewById(R.id.content) as TextView
        val metaDataView: TextView = itemView.findViewById(R.id.meta_data) as TextView
    }
}