package com.example.soundlink.features.feed.usecases

import com.example.soundlink.core.data.dto.CreatePostRequest
import com.example.soundlink.core.domain.model.Post
import com.example.soundlink.core.domain.repository.PostRepository

class CreatePostUseCase(private val PostRepository: PostRepository){
    suspend operator fun invoke(post: CreatePostRequest){
        PostRepository.createPost(post)
    }
}