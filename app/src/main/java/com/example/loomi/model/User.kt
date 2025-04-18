package com.example.loomi.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val profilePicture: String? = null,
    val coursesEnrolled: List<String> = emptyList(),
    val completedCourses: List<String> = emptyList(),
    val progress: Int = 0
)