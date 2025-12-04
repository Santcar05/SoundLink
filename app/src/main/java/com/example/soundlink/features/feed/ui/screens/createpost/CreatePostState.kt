package com.example.soundlink.features.feed.ui.screens.createpost

import android.net.Uri

data class CreatePostState(
    val title: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val media: List<Uri> = emptyList(), // Puede ser imagen o video local
    val isPosting: Boolean = false,
    val titleError: String? = null,
    val descriptionError: String? = null,
)
