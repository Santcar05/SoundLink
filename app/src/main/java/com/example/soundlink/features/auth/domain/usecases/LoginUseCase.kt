package com.example.soundlink.features.auth.domain.usecases

import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): User {
        return userRepository.login(email, password)
    }
}