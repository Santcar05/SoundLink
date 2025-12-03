package com.example.soundlink.features.feed.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.domain.usecases.GetAllPostsUseCase
import com.example.soundlink.core.domain.usecases.GetAllStoriesUseCase
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.core.ui.session.SessionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class FeedViewModel(
    private val getAllStoriesUseCase: GetAllStoriesUseCase,
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val sessionViewModel: SessionViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    init {
        loadFeed()
        loadPosts()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            try {
                // Activamos loading
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                // Llamada al caso de uso
                val stories = getAllStoriesUseCase()

                // Actualizamos el estado
                _uiState.value = _uiState.value.copy(
                    stories = stories,
                    isLoading = false
                )

            } catch (e: Exception) {
                // Manejo de errores real
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
                // Activamos loading
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                // Llamada al caso de uso
                val posts = getAllPostsUseCase()

                // Actualizamos el estado
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false
                )

            } catch (e: Exception) {
                // Manejo de errores real
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar posts",
                    isLoading = false
                )
            }
        }
    }
}
