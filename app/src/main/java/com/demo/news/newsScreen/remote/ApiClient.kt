package com.demo.news.newsScreen.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object to manage Retrofit initialization and API service creation
object ApiClient {
    // Base URL of the news API
    private const val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/"

    // Create a logging interceptor for HTTP requests and responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Set log level as needed
    }

    // Create an OkHttpClient with the logging interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .build()

    // Create a Retrofit instance with GsonConverterFactory for JSON parsing
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Set the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create an instance of the ApiService interface using Retrofit
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
