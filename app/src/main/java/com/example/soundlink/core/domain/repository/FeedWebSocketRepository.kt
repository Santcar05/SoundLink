package com.example.soundlink.core.domain.repository

import com.example. soundlink.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface FeedWebSocketRepository {
    suspend fun connect()
    fun disconnect()
    fun observeNewPosts(): Flow<Post>
    fun observePostLikes(): Flow<Post>
    fun sendPost(post: Post)
    fun sendLike(postId: Long, userId: Long)
    fun observeConnectionState(): Flow<ConnectionState>

    sealed class ConnectionState {
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
        object Disconnected : ConnectionState()
    }
}