package com.example.soundlink.core.data.repository

import com.example.soundlink.core.data.datasource.InMemoryDataSource
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataSource: InMemoryDataSource
): UserRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return dataSource.login(email, password)
    }

    override suspend fun register(username: String, email:String, password: String, age: Long): Boolean {
        return dataSource.register(username, email, password, age)
    }
    override suspend fun getCurrentUser(email: String): User? {
        return dataSource.getCurrentUser(email)
    }

}