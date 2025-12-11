package com.example.soundlink.features.profile.usecases

import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.updateUser(user)
    }
}