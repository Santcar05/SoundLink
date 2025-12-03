package com.example.soundlink.core.domain.model

data class Post(
    val id: Long,
    val user: User,
    val title: String,
    val description: String,
    val tags: List<String>,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val timestamp: Long

    //Content like image or video URL are not stored here for brevity so we used a placeholder
)
