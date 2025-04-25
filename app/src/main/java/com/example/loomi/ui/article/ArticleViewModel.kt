package com.example.loomi.ui.article


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomi.data.response.ResponseArticleItem
import com.example.loomi.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _articles = MutableStateFlow<List<ResponseArticleItem>>(emptyList())
    val articles: StateFlow<List<ResponseArticleItem>> get() = _articles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getArticles()
                _articles.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
