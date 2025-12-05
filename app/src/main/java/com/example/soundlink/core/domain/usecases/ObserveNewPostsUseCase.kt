package com.example.soundlink.core.domain.usecases

import com.example. soundlink.core.domain.model.Post
import com.example. soundlink.core.domain.repository.FeedWebSocketRepository
import kotlinx.coroutines.flow.Flow

class ObserveNewPostsUseCase(
    private val repository: FeedWebSocketRepository
) {
    operator fun invoke(): Flow<Post> {
        return repository.observeNewPosts()
    }
}