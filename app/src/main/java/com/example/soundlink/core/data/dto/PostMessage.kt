package com.example.soundlink.core.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class PostMessage(
    val id: Long?  = null,
    val user: UserDTO,  // Objeto completo
    val title: String,  // Separado
    val description: String,  // Separado
    val tags: List<String> = emptyList(),  // Tags
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val timestamp: Long
)