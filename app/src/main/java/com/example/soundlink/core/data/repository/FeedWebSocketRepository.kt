package com.example.soundlink.core.data.repository

import android.util.Log
import com. example.soundlink.core.data.dto.PostMessage
import com.example.soundlink.core.data.dto.UserDTO
import com.example.soundlink.core. data.websockets.WebSocketDataSource
import com.example.soundlink.core.data.websockets.StompClient
import com. example.soundlink.core.domain.model.Post
import com. example.soundlink.core.domain.model.User
import com. example.soundlink.core.domain.repository.FeedWebSocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FeedWebSocketRepositoryImpl(
    private val webSocketDataSource: WebSocketDataSource
) : FeedWebSocketRepository {

    override suspend fun connect() {
        Log.d("FeedWSRepo", "🔌 Conectando WebSocket...")
        webSocketDataSource.connect()
        Log. d("FeedWSRepo", "✅ WebSocket conectado")
    }

    override fun disconnect() {
        Log.d("FeedWSRepo", "🛑 Desconectando WebSocket...")
        webSocketDataSource.disconnect()
    }

    override fun observeNewPosts(): Flow<Post> {
        Log.d("FeedWSRepo", "👀 Observando nuevos posts...")
        return webSocketDataSource.observeNewPosts()
            .map { postMessage ->
                Log.d("FeedWSRepo", "📦 Mapeando PostMessage a Post")
                Log.d("FeedWSRepo", "   ID: ${postMessage.id}")
                Log.d("FeedWSRepo", "   Title: ${postMessage.title}")
                Log.d("FeedWSRepo", "   User: ${postMessage.user.name}")
                Log.d("FeedWSRepo", "   Tags: ${postMessage.tags}")
                postMessage.toDomain()
            }
    }

    override fun observePostLikes(): Flow<Post> {
        return webSocketDataSource.observePostLikes()
            .map { postMessage -> postMessage.toDomain() }
    }

    override fun sendPost(post: Post) {
        Log.d("FeedWSRepo", "📤 Enviando post por WebSocket...")

        // Crear UserDTO completo
        val userDTO = UserDTO(
            id = post.user.id,
            name = post.user.name,
            email = post.user.email,
            avatarUrl = post.user.avatarUrl,
            verified = post.user.verified
        )

        // Crear PostMessage completo
        val postMessage = PostMessage(
            id = post.id,
            user = userDTO,
            title = post.title,
            description = post.description,
            tags = post.tags,
            likes = post.likes,
            comments = post.comments,
            shares = post.shares,
            timestamp = post.timestamp
        )

        webSocketDataSource.sendPost(postMessage)
        Log.d("FeedWSRepo", "✅ Post enviado")
    }

    override fun sendLike(postId: Long, userId: Long) {
        Log.d("FeedWSRepo", "👍 Enviando like...")

        // Crear un UserDTO básico para el like
        val userDTO = UserDTO(
            id = userId,
            name = "",
            email = "",
            avatarUrl = "",
            verified = false
        )

        val postMessage = PostMessage(
            id = postId,
            user = userDTO,
            title = "",
            description = "",
            tags = emptyList(),
            likes = 1,
            comments = 0,
            shares = 0,
            timestamp = System.currentTimeMillis()
        )

        webSocketDataSource.sendLike(postMessage)
    }

    override fun observeConnectionState(): Flow<FeedWebSocketRepository.ConnectionState> {
        return webSocketDataSource.observeConnectionState()
            .map { state ->
                when (state) {
                    is StompClient.ConnectionState.Connecting ->
                        FeedWebSocketRepository. ConnectionState.Connecting
                    is StompClient.ConnectionState. Connected ->
                        FeedWebSocketRepository.ConnectionState. Connected
                    is StompClient.ConnectionState.Error ->
                        FeedWebSocketRepository.ConnectionState.Error(state.message)
                    is StompClient. ConnectionState.Disconnected ->
                        FeedWebSocketRepository. ConnectionState.Disconnected
                }
            }
    }

    // ✅ Mapper actualizado: PostMessage completo → Post domain
    private fun PostMessage.toDomain(): Post {
        return Post(
            id = this. id ?: System.currentTimeMillis(),
            user = User(
                id = this.user.id,
                name = this.user. name,
                email = this. user.email,
                password = "",  // No se envía por seguridad
                age = 0,  // No necesario para mostrar en feed
                avatarUrl = this.user.avatarUrl,
                verified = this.user.verified
            ),
            title = this.title,
            description = this.description,
            tags = this.tags,
            likes = this.likes,
            comments = this.comments,
            shares = this.shares,
            timestamp = this.timestamp
        )
    }
}