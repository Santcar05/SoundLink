package com.example.soundlink.core.data.websockets

import kotlinx. coroutines.flow.Flow
import kotlinx.coroutines. flow.filter
import kotlinx. coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json. Json
import android.util.Log
import com.example.soundlink.core.data.dto.PostMessage
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.withTimeoutOrNull

class WebSocketDataSource(
    private val stompClient: StompClient,
    private val json: Json
) {

    suspend fun connect() {
        Log. d("WebSocketDataSource", "🔌 Conectando StompClient...")
        stompClient.connect()

        // ESPERAR a que STOMP esté conectado ANTES de suscribirse
        Log.d("WebSocketDataSource", "⏳ Esperando confirmación CONNECTED de STOMP...")
        val connected = withTimeoutOrNull(10000) {
            stompClient.connectionState.first { state ->
                Log.d("WebSocketDataSource", "Estado recibido: $state")
                state is StompClient.ConnectionState.Connected
            }
        }

        if (connected != null) {
            Log.d("WebSocketDataSource", "✅ STOMP conectado exitosamente!")
            Log.d("WebSocketDataSource", "📝 Suscribiendo a /topic/feed...")
            stompClient.subscribe("/topic/feed")
            Log.d("WebSocketDataSource", "✅ Suscripción completada")
        } else {
            Log.e("WebSocketDataSource", "❌ Timeout esperando conexión STOMP (10 segundos)")
        }
    }

    fun disconnect() {
        Log. d("WebSocketDataSource", "🛑 Desconectando StompClient...")
        stompClient.disconnect()
    }

    fun observeConnectionState(): Flow<StompClient.ConnectionState> {
        return stompClient. connectionState
    }

    // Observar nuevos posts en el feed
    fun observeNewPosts(): Flow<PostMessage> {
        Log.d("WebSocketDataSource", "👀 Configurando Flow para observar nuevos posts...")

        // ✅ NO suscribirse aquí, ya se hizo en connect()

        return stompClient.messages
            .filter { message ->
                val matches = message.destination == "/topic/feed"
                Log.d("WebSocketDataSource", "Mensaje filtrado: destination=${message.destination}, matches=$matches")
                matches
            }
            .map { message ->
                Log.d("WebSocketDataSource", "📦 Nuevo post recibido!")
                Log.d("WebSocketDataSource", "   Destination: ${message.destination}")
                Log.d("WebSocketDataSource", "   Body: ${message.body}")
                json.decodeFromString<PostMessage>(message.body)
            }
    }

    // Observar likes en posts
    fun observePostLikes(): Flow<PostMessage> {
        // Solo suscribirse si aún no lo has hecho
        stompClient.subscribe("/topic/feed-likes")

        return stompClient.messages
            .filter { it.destination == "/topic/feed-likes" }
            .map { message ->
                Log.d("WebSocketDataSource", "📦 Post like received: ${message.body}")
                json.decodeFromString<PostMessage>(message.body)
            }
    }

    // Enviar un nuevo post
    fun sendPost(postMessage: PostMessage) {
        val body = json.encodeToString(postMessage)
        Log.d("WebSocketDataSource", "📤 Enviando post: $body")
        stompClient. send("/app/posts/create", body)
    }

    // Enviar un like
    fun sendLike(postMessage: PostMessage) {
        val body = json.encodeToString(postMessage)
        Log.d("WebSocketDataSource", "📤 Enviando like: $body")
        stompClient.send("/app/posts/like", body)
    }

    fun unsubscribeFromFeed() {
        Log.d("WebSocketDataSource", "📝 Desuscribiendo de /topic/feed")
        stompClient.unsubscribe("/topic/feed")
    }

    fun unsubscribeFromLikes() {
        Log.d("WebSocketDataSource", "📝 Desuscribiendo de /topic/feed-likes")
        stompClient.unsubscribe("/topic/feed-likes")
    }
}