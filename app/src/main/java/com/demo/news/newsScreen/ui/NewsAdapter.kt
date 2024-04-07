package com.demo.news.newsScreen.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.news.databinding.ListItemNewsBinding
import com.demo.news.newsScreen.data.model.Article
import com.bumptech.glide.Glide
import com.demo.news.R

// RecyclerView adapter for displaying news articles
class NewsAdapter : ListAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // Inflate the item layout and create a ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // Bind data to ViewHolder
        val article = getItem(position)
        holder.bind(article)
    }

    inner class NewsViewHolder(private val binding: ListItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener for opening article in browser
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val article = getItem(position)
                    openArticleInBrowser(binding.root.context, article.url)
                }
            }
        }

        fun bind(article: Article) {
            // Bind article data to the item layout
            binding.article = article

            // Load article image using Glide
            Glide.with(binding.root)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageView)

            binding.executePendingBindings()
        }
    }

    // Open the article in a web browser
    private fun openArticleInBrowser(context: Context, articleUrl: String) {
        val intent = Intent(context, NewsWebActivity::class.java).apply {
            putExtra(NewsWebActivity.EXTRA_URL, articleUrl)
        }
        context.startActivity(intent)
    }

    // DiffUtil callback for calculating item changes
    private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}
