package com.example.soundlink.core.domain.repository

import com.example.soundlink.core.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(name: String, email: String, password: String, age: Long): Boolean

    suspend fun getCurrentUser(email: String): User?
}