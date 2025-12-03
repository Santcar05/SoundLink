package com.example.soundlink.core.data.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val age: Int
)