package com.reindeercrafts.hackernews

import android.graphics.Rect
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.reindeercrafts.hackernews.data.Article
import com.reindeercrafts.hackernews.data.ArticleRepository
import java.util.*

class ArticleListController(view: View, private val repository: ArticleRepository,
                            val onSelectedListener: (Article) -> Unit) {
    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
    private val refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refresh_layout) as SwipeRefreshLayout
    private val comparator: Comparator<Article> = Comparator { (_, _, _, _, time), (_, _, _, _, time1) ->
        time.compareTo(time1)
    }

    init {

        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager

        val margin = view.resources.getDimensionPixelSize(R.dimen.generic_padding_margin_med)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?,
                                        state: RecyclerView.State?) {
                val position = parent!!.getChildAdapterPosition(view)
                if (position == 0) {
                    outRect!!.set(margin * 2, margin, margin * 2, margin / 2)
                } else if (position == parent.adapter.itemCount - 1) {
                    outRect!!.set(margin * 2, margin / 2, margin * 2, margin)
                } else {
                    outRect!!.set(margin * 2, margin / 2, margin * 2, margin / 2)
                }
            }
        })

        recyclerView.setOnScrollChangeListener { view, _, _, _, _ ->
            refreshLayout.isEnabled = !view.canScrollVertically(-1)
        }

        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = true
            repository.setArticleCacheDirty(true)
            repository.getArticles("story", comparator, {}, {
                refreshLayout.isRefreshing = false
                if (it != null) {
                    (recyclerView.adapter as ArticleAdapter).prepend(it)
                    if (it.isNotEmpty()) {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            })
        }

        update(true)
    }

    fun update(cacheDirty: Boolean) {
        repository.setArticleCacheDirty(cacheDirty)
        repository.getArticles("story", comparator, {
            recyclerView.swapAdapter(ArticleAdapter(ArrayList(it), onSelectedListener), false)
        }, {
            if (it != null) {
                if (recyclerView.adapter is ArticleAdapter) {
                    (recyclerView.adapter as ArticleAdapter).prepend(it)
                } else {
                    recyclerView.swapAdapter(ArticleAdapter(ArrayList(it), onSelectedListener), false)
                }
            }
        })
    }


    class ArticleAdapter(val articles: MutableList<Article>, val onSelectedListener: (Article) -> Unit) :
            RecyclerView.Adapter<ArticleViewHolder>() {

        override fun onBindViewHolder(holder: ArticleViewHolder?, position: Int) {
            holder!!.titleView.text = articles[position].title
            holder.timeView.text = DateUtils.getRelativeDateTimeString(holder.itemView.context,
                    articles[position].time * 1000,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
            holder.itemView.setOnClickListener {
                onSelectedListener.invoke(articles[position])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArticleViewHolder {
            return ArticleViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.view_article_list, parent,
                    false))
        }

        override fun getItemCount(): Int {
            return articles.size
        }

        fun prepend(newArticles: List<Article>) {
            articles.addAll(0, newArticles)
            notifyItemRangeInserted(0, newArticles.size)
        }

    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.title) as TextView
        val timeView: TextView = itemView.findViewById(R.id.time) as TextView
    }
}