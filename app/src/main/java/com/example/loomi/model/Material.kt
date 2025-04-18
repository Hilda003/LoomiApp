package com.example.loomi.model

import java.util.UUID

data class Material(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val imgResId: Int? = null,
    val sections: List<Section>,
    var isComplete: Boolean = false
)