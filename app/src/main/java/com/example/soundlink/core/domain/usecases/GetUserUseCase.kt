package com.example.soundlink.core.domain.usecases

import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String): User? {
        return userRepository.getCurrentUser(email)
    }
}