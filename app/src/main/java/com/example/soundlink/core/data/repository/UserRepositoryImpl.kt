package com.example.soundlink.core.data.repository

import com.example.soundlink.core.data.datasource.InMemoryDataSource
import com.example.soundlink.core.data.datasource.retrofitspringboot.UserApi
import com.example.soundlink.core.data.dto.LoginRequest
import com.example.soundlink.core.data.dto.RegisterRequest
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataSource: UserApi
): UserRepository {

    override suspend fun login(email: String, password: String): User {
        return dataSource.login(LoginRequest(email, password))
    }

    override suspend fun register(username: String, email:String, password: String, age: Int): User {
        return dataSource.register(RegisterRequest(username, email, password, age))
    }
    override suspend fun getCurrentUser(email: String): User? {
        return dataSource.getUserByEmail(email)
    }

}