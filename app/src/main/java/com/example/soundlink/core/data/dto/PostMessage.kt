package com.example.soundlink.core.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class PostMessage(
    val id: Long?  = null,
    val userId: Long,
    val content: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val timestamp: Long
)