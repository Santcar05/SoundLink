package com.example.soundlink.core.data.datasource

import com.example.soundlink.core.domain.model.User
import kotlin.collections.any
import kotlin.collections.firstOrNull

class InMemoryDataSource {

    private val users = mutableListOf(
        User("name1", "user1@email.com", "pass1", 18),
        User("name2", "user2@email.com", "pass2", 20),
        User("name3", "user3@email.com", "pass3", 24),
        User("name4", "user4@email.com", "pass4", 26),
        User("name5", "user5@email.com", "pass5", 28)
    )
    suspend fun login(email: String, password: String): Boolean {
        println("Usuarios actuales: $users")
        return users.any { it.email == email  && it.password == password }
    }


    suspend fun register(name: String, email: String, password: String, age: Int): Boolean {
        return users.add(User(name, email, password, age))
    }


    suspend fun logout() {

    }

    suspend fun getCurrentUser(): User? {
        return users.firstOrNull()
    }

    suspend fun getAllUsers(): List<User> {
        return users
    }


    suspend fun getCurrentUser(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

}