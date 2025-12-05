package com.example.soundlink.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String = "",
    val verified: Boolean = false
)