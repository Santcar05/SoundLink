package com.example.soundlink.core.data.datasource

import com.example.soundlink.core.domain.model.User
import kotlin.collections.any
import kotlin.collections.firstOrNull

class InMemoryDataSource {

    private val users = mutableListOf<User>()

    suspend fun login(username: String, password: String): Boolean {
        return users.any { it.name == username && it.password == password }
    }


    suspend fun register(name: String, email: String, password: String, age: Int): Boolean {
        return users.add(User(name, email, password, age))
    }


    suspend fun logout() {
        users.clear()
    }

    suspend fun getCurrentUser(): User? {
        return users.firstOrNull()
    }

    suspend fun getAllUsers(): List<User> {
       //Agregar 5 usuarios a la lista
        users.add(User( "name1", "user1@email.com","pass1", 18))
        users.add(User( "name2", "user2@email.com","pass2", 20))
        users.add(User( "name3", "user3@email.com","pass3",24))
        users.add(User( "name4", "user4@email.com","pass4",26))
        users.add(User( "name5", "user5@email.com","pass5",28))
        return users
    }


    suspend fun getCurrentUser(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

}