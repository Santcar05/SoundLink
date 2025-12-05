package com.example.soundlink.core.data.repository

import com.example.soundlink.core.data.dto.PostMessage
import com.example.soundlink.core.data.websockets.WebSocketDataSource
import com.example.soundlink.core.data.websockets.StompClient
import com.example.soundlink.core.domain.model.Post
import com.example. soundlink.core.domain.model.User
import com.example. soundlink.core.domain.repository.FeedWebSocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedWebSocketRepositoryImpl(
    private val webSocketDataSource: WebSocketDataSource
) : FeedWebSocketRepository {

    override suspend fun connect() {
        webSocketDataSource.connect()
    }

    override fun disconnect() {
        webSocketDataSource.disconnect()
    }

    override fun observeNewPosts(): Flow<Post> {
        return webSocketDataSource.observeNewPosts()
            .map { postMessage -> postMessage.toDomain() }
    }

    override fun observePostLikes(): Flow<Post> {
        return webSocketDataSource. observePostLikes()
            .map { postMessage -> postMessage.toDomain() }
    }

    override fun sendPost(post: Post) {
        val postMessage = PostMessage(
            id = post.id,
            userId = post.user.id,
            content = "${post.title}|${post.description}",
            imageUrl = null,
            likes = post.likes,
            timestamp = post.timestamp
        )
        webSocketDataSource.sendPost(postMessage)
    }

    override fun sendLike(postId: Long, userId: Long) {
        val postMessage = PostMessage(
            id = postId,
            userId = userId,
            content = "",
            likes = 1,
            timestamp = System.currentTimeMillis()
        )
        webSocketDataSource.sendLike(postMessage)
    }

    override fun observeConnectionState(): Flow<FeedWebSocketRepository.ConnectionState> {
        return webSocketDataSource.observeConnectionState()
            .map { state ->
                when (state) {
                    is StompClient.ConnectionState.Connecting ->
                        FeedWebSocketRepository.ConnectionState.Connecting
                    is StompClient.ConnectionState.Connected ->
                        FeedWebSocketRepository.ConnectionState. Connected
                    is StompClient.ConnectionState.Error ->
                        FeedWebSocketRepository. ConnectionState.Error(state.message)
                    is StompClient.ConnectionState.Disconnected ->
                        FeedWebSocketRepository.ConnectionState.Disconnected
                }
            }
    }

    // Mapper de PostMessage a Post
    private fun PostMessage.toDomain(): Post {
        val parts = this.content.split("|")
        val title = parts. getOrNull(0) ?: ""
        val description = parts. getOrNull(1) ?: ""

        return Post(
            id = this.id ?: 0,
            user = User(), // Deberías obtener el usuario real del backend
            title = title,
            description = description,
            tags = emptyList(),
            likes = this.likes,
            comments = 0,
            shares = 0,
            timestamp = this.timestamp
        )
    }
}