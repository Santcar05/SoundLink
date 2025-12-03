package com.example.soundlink.core.domain.usecases

import com.example.soundlink.core.domain.model.Post
import com.example.soundlink.core.domain.repository.PostRepository

class GetAllPostsUseCase(private val postRepository: PostRepository) {
    suspend operator fun invoke(): List<Post> {
        return postRepository.getAllPosts()
    }
}