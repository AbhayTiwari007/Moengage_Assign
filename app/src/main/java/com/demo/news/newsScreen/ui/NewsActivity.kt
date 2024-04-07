package com.demo.news.newsScreen.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.news.databinding.ActivityNewsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter

    private val notificationPermissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up data binding
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and adapter
        adapter = NewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe news articles
        viewModel.newsArticles.observe(this) { articles ->
            adapter.submitList(articles)
            adapter.notifyDataSetChanged()
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        // Observe errors
        viewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        // Fetch news articles
        viewModel.fetchNewsArticles()

        // Set up the floating action button click listener
        binding.fabSort.setOnClickListener {
            adapter.submitList(null)
            viewModel.toggleSortOrder()
            adapter.notifyDataSetChanged()
        }

        // Check for notification permission
        if (!hasNotificationPermission()) {
            // Request permission
            requestNotificationPermission()
        } else {
            // Subscribe to the "cricket" topic for Firebase Cloud Messaging
            firebaseSetup()
        }
    }

    // Check if notification permission is granted
    private fun hasNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request notification permission
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            notificationPermissionRequestCode
        )
    }

    // Set up Firebase messaging
    private fun firebaseSetup() {
        FirebaseMessaging.getInstance().subscribeToTopic("cricket")
            .addOnCompleteListener { task ->
                val msg = if (task.isSuccessful) "Subscribed to cricket topic" else "Failed to subscribe"
                Log.d(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

    // Handle notification permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firebaseSetup()
            } else {
                // Permission denied
                Toast.makeText(baseContext, "Please grant notification permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
