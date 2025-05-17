package com.example.loomi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomi.data.model.Article
import com.example.loomi.data.model.UserProgress
import com.example.loomi.data.retrofit.ApiConfig
import com.example.loomi.utils.ProgressManager.fetchMaterialProgressSummary

import com.example.loomi.utils.toArticle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _overallProgress = MutableLiveData<Triple<Int, Int, Int>>()
    val overallProgress: LiveData<Triple<Int, Int, Int>> = _overallProgress

    private val _latestArticle = MutableLiveData<Article?>()
    val latestArticle: LiveData<Article?> = _latestArticle

    fun fetchOverallProgress(userId: String) {
        viewModelScope.launch {
            try {
                val materialsSnapshot = firestore.collection("materials").get().await()
                if (materialsSnapshot.isEmpty) {
                    _overallProgress.value = Triple(0, 0, 0)
                    return@launch
                }

                var totalSections = 0
                var completedSections = 0

                val jobs = materialsSnapshot.documents.map { materialDoc ->
                    async {
                        fetchMaterialProgressSummary(userId, materialDoc.id)
                    }
                }

                val results = jobs.awaitAll()
                results.forEach { (completed, total) ->
                    completedSections += completed
                    totalSections += total
                }

                val percentage = if (totalSections > 0)
                    (completedSections * 100) / totalSections
                else 0

                _overallProgress.value = Triple(completedSections, totalSections, percentage)
            } catch (e: Exception) {
                Log.e(TAG, "Error calculating overall progress", e)
                _overallProgress.value = Triple(0, 0, 0)
            }
        }
    }

    fun fetchLatestArticle() {
        Log.d(TAG, "Fetching latest article from API...")

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getArticles()
                }

                _latestArticle.value = response.firstOrNull()?.toArticle()

                Log.d(TAG, "Latest article fetched: ${_latestArticle.value?.title}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch articles", e)
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}