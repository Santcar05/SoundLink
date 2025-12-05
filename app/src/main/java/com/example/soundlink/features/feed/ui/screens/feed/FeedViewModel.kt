package com.example.soundlink.features.feed.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.domain.usecases.*
import com.example.soundlink.core. ui.session.SessionViewModel
import kotlinx.coroutines.flow. MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines. launch
import android.util.Log

class FeedViewModel(
    private val getAllStoriesUseCase: GetAllStoriesUseCase,
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val sessionViewModel: SessionViewModel,
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
    private val disconnectWebSocketUseCase: DisconnectWebSocketUseCase,
    private val observeNewPostsUseCase: ObserveNewPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    init {
        Log. d("FeedViewModel", "========================================")
        Log.d("FeedViewModel", "🚀 FEED VIEWMODEL INICIALIZADO")
        Log. d("FeedViewModel", "========================================")

        loadStories()
        loadPosts()
        connectToWebSocket()
        observeNewPosts()
    }

    private fun connectToWebSocket() {
        viewModelScope.launch {
            try {
                Log. d("FeedViewModel", "🔌 Intentando conectar WebSocket...")
                connectWebSocketUseCase()
                Log. d("FeedViewModel", "✅ WebSocket connect() llamado exitosamente")
            } catch (e: Exception) {
                Log.e("FeedViewModel", "❌ Error conectando WebSocket", e)
            }
        }
    }

    private fun observeNewPosts() {
        viewModelScope.launch {
            observeNewPostsUseCase(). collect { newPost ->
                Log.d("FeedViewModel", "New post received via WebSocket: ${newPost.title}")

                // Agregar el nuevo post al principio de la lista
                val currentPosts = _uiState. value.posts.toMutableList()
                currentPosts.add(0, newPost)

                _uiState.value = _uiState.value.copy(
                    posts = currentPosts
                )

                Log.d("FeedViewModel", "✅ Post agregado.  Total posts: ${currentPosts.size}")
            }
        }
    }

    private fun loadStories() {
        viewModelScope.launch {
            try {
                _uiState. value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val stories = getAllStoriesUseCase()

                _uiState.value = _uiState.value.copy(
                    stories = stories,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar historias",
                    isLoading = false
                )
            }
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                Log.d("FeedViewModel", "📥 Cargando posts iniciales...")
                val posts = getAllPostsUseCase()
                Log.d("FeedViewModel", "✅ ${posts.size} posts iniciales cargados")

                _uiState. value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false
                )

            } catch (e: Exception) {
                Log.e("FeedViewModel", "❌ Error cargando posts", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar posts",
                    isLoading = false
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocketUseCase()
        Log.d("FeedViewModel", "WebSocket disconnected")
    }
}