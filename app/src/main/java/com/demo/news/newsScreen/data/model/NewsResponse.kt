package com.demo.news.newsScreen.data.model

// Model class representing the response from the news API
data class NewsResponse(
    val status: String,         // Status of the response (e.g., "ok", "error")
    val articles: List<Article> // List of articles retrieved from the API
)

// Model class representing a news article
data class Article(
    val source: Source,         // Information about the source of the article
    val author: String?,        // Author of the article (nullable)
    val title: String,          // Title of the article
    val description: String?,   // Description of the article (nullable)
    val url: String,            // URL of the article
    val urlToImage: String?,    // URL of the article's image (nullable)
    val publishedAt: String,    // Date and time when the article was published
    val content: String?        // Content of the article (nullable)
)

// Model class representing the source of a news article
data class Source(
    val id: String?,   // ID of the news source (nullable)
    val name: String   // Name of the news source
)
