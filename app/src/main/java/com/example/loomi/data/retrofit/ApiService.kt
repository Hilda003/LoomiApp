package com.example.loomi.data.retrofit

import com.example.loomi.data.model.Article
import com.example.loomi.data.response.ResponseArticle
import com.example.loomi.data.response.ResponseArticleItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun getArticles(
        @Query("tag") tag: String = "android"
    ): List<ResponseArticleItem>
}
