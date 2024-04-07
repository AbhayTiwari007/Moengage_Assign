package com.demo.news.newsScreen.domain.repository

import com.demo.news.newsScreen.data.model.Article
import com.demo.news.newsScreen.remote.ApiClient

class NewsRepository {
    // Function to fetch news articles from the API
    suspend fun getNewsArticles(): List<Article>? {
        return try {
            // Call the API service to get the response
            val response = ApiClient.apiService.getNewsArticles()

            // Check if the response is successful
            if (response.isSuccessful) {
                // Return the list of articles if the response is successful
                response.body()?.articles
            } else {
                // Return null if the response is not successful
                null
            }
        } catch (e: Exception) {
            // Return null if an exception occurs during the API call
            null
        }
    }
}
