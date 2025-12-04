package com.example.soundlink.features.feed.ui.screens.createpost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundlink.core.data.dto.CreatePostRequest
import com.example.soundlink.core.domain.model.Post
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.features.feed.usecases.CreatePostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val sessionViewModel: SessionViewModel
) : ViewModel() {
    private val _state = MutableStateFlow(CreatePostState())
    val state: StateFlow<CreatePostState> = _state

    fun onTitleChange(newTitle: String) {
        _state.update { it.copy(title = newTitle, titleError = null) }
    }

    fun onDescriptionChange(newDescription: String) {
        _state.update { it.copy(description = newDescription, descriptionError = null) }
    }

    fun onTagsChange(newTags: List<String>) {
        _state.update { it.copy(tags = newTags) }
    }

    fun onAddMedia(uri: Uri) {
        _state.update { it.copy(media = it.media + uri) }
    }

    fun onRemoveMedia(uri: Uri) {
        _state.update { it.copy(media = it.media.filterNot { m -> m == uri }) }
    }

    fun validateAndPost(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (sessionViewModel.state.value.user == null) {
            onError("Por favor, inicia sesión.")
            return
        }
        viewModelScope.launch {

            val curr = _state.value
            var valid = true
            var titleError: String? = null
            var descriptionError: String? = null

            if (curr.title.isBlank()) {
                valid = false
                titleError = "El título no puede estar vacío"
            }
            if (curr.description.isBlank()) {
                valid = false
                descriptionError = "La descripción no puede estar vacía"
            }
            _state.update { it.copy(titleError = titleError, descriptionError = descriptionError) }

            if (valid) {
                _state.update { it.copy(isPosting = true) }
                // Create Post Creation
                val post = CreatePostRequest(
                    userId = sessionViewModel.state.value.user?.id!!,
                    title = curr.title,
                    description = curr.description,
                    tags = curr.tags,
                    timestamp = System.currentTimeMillis()
                )
                createPostUseCase(post)
                onSuccess()
                _state.update { it.copy(isPosting = false) }
            } else {
                onError("Por favor, corrige los campos.")
            }
        }
    }
}