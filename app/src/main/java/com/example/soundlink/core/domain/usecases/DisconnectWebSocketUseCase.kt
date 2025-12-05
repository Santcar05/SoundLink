package com.example.soundlink.core.domain.usecases

import com.example.soundlink.core.domain.repository. FeedWebSocketRepository

class DisconnectWebSocketUseCase(
    private val repository: FeedWebSocketRepository
) {
    operator fun invoke() {
        repository.disconnect()
    }
}