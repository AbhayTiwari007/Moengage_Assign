package com.demo.news.newsScreen.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.demo.news.R

// Activity to display news articles in a WebView
class NewsWebActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web)

        // Initialize WebView and ProgressBar
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        // Load the URL passed from the intent
        val url = intent.getStringExtra(EXTRA_URL)
        if (url != null) {
            setupWebView()
            webView.loadUrl(url)
        } else {
            // Handle error or show message if URL is null
        }
    }

    // Configure WebView settings and setup WebViewClient
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show progress bar while page is loading
                progressBar.visibility = View.VISIBLE
                webView.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide progress bar when page has finished loading
                progressBar.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }
        }

        // Enable JavaScript for the WebView
        val settings = webView.settings
        settings.javaScriptEnabled = true
    }
}
