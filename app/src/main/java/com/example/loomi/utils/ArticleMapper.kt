package com.example.loomi.utils

import com.example.loomi.data.model.Article
import com.example.loomi.data.response.ResponseArticleItem

fun ResponseArticleItem.toArticle(): Article {
    return Article(
        id = this.id,
        title = this.title,
        description = this.description,
        publishedAt = this.publishedAt,
        coverImage = this.coverImage?.toString()?.takeIf { it != "null" },
        authorName = this.user.name,
        url = this.url
    )
}

