package com.example.soundlink.core.data.dto

data class CreatePostRequest(
    val userId: Long,
    val title: String,
    val description: String,
    val tags: List<String>,
    val timestamp: Long
)
