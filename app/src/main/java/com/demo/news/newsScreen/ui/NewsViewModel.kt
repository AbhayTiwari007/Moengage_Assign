package com.demo.news.newsScreen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.news.newsScreen.data.model.Article
import com.demo.news.newsScreen.domain.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    // Repository instance to fetch news articles
    private val repository = NewsRepository()

    // LiveData for holding the list of news articles
    private val _newsArticles = MutableLiveData<List<Article>?>()
    val newsArticles: LiveData<List<Article>?> get() = _newsArticles

    // LiveData for tracking the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData for holding error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Flag to toggle sorting order
    private var isSortedNewToOld = false // Initially sorted from new to old

    // Function to fetch news articles from the repository
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

    // Function to toggle sorting order based on publication date
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
