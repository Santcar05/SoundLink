package com.example.soundlink.features.feed.ui.screens.feed

import com.example.soundlink.core.domain.model.Post
import com.example.soundlink.core.domain.model.Story

data class FeedState (
    val stories: List<Story> = emptyList(),
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
    )