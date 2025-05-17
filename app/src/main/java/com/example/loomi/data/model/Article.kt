package com.example.loomi.data.model


data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val publishedAt: String,
    val coverImage: String?,
    val authorName: String,
    val url: String
)
