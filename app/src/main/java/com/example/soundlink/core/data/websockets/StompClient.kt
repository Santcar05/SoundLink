package com.example.soundlink.core.data.websockets

import kotlinx.coroutines.flow. Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import okio.ByteString
import android.util.Log
import kotlinx.coroutines. CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx. serialization.json.Json

class StompClient(
    private val url: String,
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<StompMessage>(replay = 0)
    val messages: Flow<StompMessage> = _messages.asSharedFlow()

    private val _connectionState = MutableSharedFlow<ConnectionState>(replay = 1)
    val connectionState: Flow<ConnectionState> = _connectionState.asSharedFlow()

    private val subscriptions = mutableMapOf<String, String>()
    private var subscriptionId = 0

    sealed class ConnectionState {
        object Connecting : ConnectionState()
        object Connected : ConnectionState()
        data class Error(val message: String) : ConnectionState()
        object Disconnected : ConnectionState()
    }

    data class StompMessage(
        val destination: String,
        val body: String
    )

    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            _connectionState.emit(ConnectionState. Connecting)
        }

        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("StompClient", "WebSocket opened")
                // Enviar frame CONNECT de STOMP
                val connectFrame = """
                    CONNECT
                    accept-version:1.1,1.2
                    heart-beat:10000,10000
                    
                    ${'\u0000'}
                """.trimIndent()
                webSocket. send(connectFrame)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log. d("StompClient", "Message received: $text")
                handleStompFrame(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log. d("StompClient", "Binary message received")
                handleStompFrame(bytes.utf8())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("StompClient", "WebSocket closing: $reason")
                CoroutineScope(Dispatchers.IO).launch {
                    _connectionState.emit(ConnectionState.Disconnected)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("StompClient", "WebSocket error", t)
                CoroutineScope(Dispatchers.IO). launch {
                    _connectionState.emit(ConnectionState.Error(t.message ?: "Unknown error"))
                }
            }
        })
    }

    private fun handleStompFrame(frame: String) {
        val lines = frame.split("\n")
        val command = lines. firstOrNull() ?: return

        when (command) {
            "CONNECTED" -> {
                Log. d("StompClient", "STOMP connected")
                CoroutineScope(Dispatchers.IO).launch {
                    _connectionState.emit(ConnectionState.Connected)
                }
            }
            "MESSAGE" -> {
                val headers = mutableMapOf<String, String>()
                var bodyStartIndex = 0

                for (i in 1 until lines.size) {
                    if (lines[i].isEmpty()) {
                        bodyStartIndex = i + 1
                        break
                    }
                    val parts = lines[i].split(":", limit = 2)
                    if (parts.size == 2) {
                        headers[parts[0]] = parts[1]
                    }
                }

                val body = lines.drop(bodyStartIndex)
                    .joinToString("\n")
                    .replace("\u0000", "")
                    .trim()

                val destination = headers["destination"] ?: ""

                CoroutineScope(Dispatchers.IO).launch {
                    _messages.emit(StompMessage(destination, body))
                }
            }
            "ERROR" -> {
                Log.e("StompClient", "STOMP error: $frame")
                CoroutineScope(Dispatchers.IO). launch {
                    _connectionState.emit(ConnectionState.Error("STOMP error"))
                }
            }
        }
    }

    fun subscribe(destination: String): String {
        val id = "sub-${subscriptionId++}"
        subscriptions[destination] = id

        val subscribeFrame = """
            SUBSCRIBE
            id:$id
            destination:$destination
            
            ${'\u0000'}
        """.trimIndent()

        webSocket?.send(subscribeFrame)
        Log.d("StompClient", "Subscribed to $destination with id $id")
        return id
    }

    fun unsubscribe(destination: String) {
        val id = subscriptions[destination] ?: return

        val unsubscribeFrame = """
            UNSUBSCRIBE
            id:$id
            
            ${'\u0000'}
        """.trimIndent()

        webSocket?.send(unsubscribeFrame)
        subscriptions.remove(destination)
        Log.d("StompClient", "Unsubscribed from $destination")
    }

    fun send(destination: String, body: String) {
        val sendFrame = """
            SEND
            destination:$destination
            content-type:application/json
            
            $body${'\u0000'}
        """.trimIndent()

        webSocket?.send(sendFrame)
        Log.d("StompClient", "Sent to $destination: $body")
    }

    fun disconnect() {
        val disconnectFrame = """
            DISCONNECT
            
            ${'\u0000'}
        """.trimIndent()

        webSocket?.send(disconnectFrame)
        webSocket?.close(1000, "Client disconnect")
        webSocket = null

        CoroutineScope(Dispatchers.IO).launch {
            _connectionState.emit(ConnectionState.Disconnected)
        }
    }
}