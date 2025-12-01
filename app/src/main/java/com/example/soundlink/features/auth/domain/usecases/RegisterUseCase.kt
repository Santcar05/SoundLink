package com.example.soundlink.features.auth.domain.usecases

import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, email: String, password: String, age: Long): User {
        return userRepository.register(name, email, password, age)
    }
}