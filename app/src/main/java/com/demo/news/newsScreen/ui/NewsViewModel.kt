package com.demo.news.newsScreen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.news.newsScreen.data.model.Article
import com.demo.news.newsScreen.domain.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _newsArticles = MutableLiveData<List<Article>?>()
    val newsArticles: LiveData<List<Article>?> get() = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var isSortedNewToOld = false // Initially sorted from new to old

    fun fetchNewsArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val articles = repository.getNewsArticles()
                _newsArticles.value = articles
            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching news articles"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleSortOrder() {
        val sortedArticles = if (isSortedNewToOld) {
            newsArticles.value?.sortedByDescending { it.publishedAt }
        } else {
            newsArticles.value?.sortedBy { it.publishedAt }
        }
        _newsArticles.value = sortedArticles
        isSortedNewToOld = !isSortedNewToOld
    }
}
