package com.demo.news.newsScreen.remote

import com.demo.news.newsScreen.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

// Retrofit interface defining the API endpoints
interface ApiService {
    // Endpoint to fetch news articles
    @GET("staticResponse.json")
    suspend fun getNewsArticles(): Response<NewsResponse>
}
