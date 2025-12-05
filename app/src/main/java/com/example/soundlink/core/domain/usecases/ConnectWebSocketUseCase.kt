package com.example.soundlink.core.domain.usecases

import com.example.soundlink.core.domain.repository.FeedWebSocketRepository

class ConnectWebSocketUseCase(
    private val repository: FeedWebSocketRepository
) {
    suspend operator fun invoke() {
        repository.connect()
    }
}
