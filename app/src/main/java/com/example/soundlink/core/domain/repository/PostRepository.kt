package com.example.soundlink.core.domain.repository

import com.example.soundlink.core.domain.model.Post

interface PostRepository {
    suspend fun getAllPosts(): List<Post>
    suspend fun getPostById(id: Long): Post
    suspend fun createPost(post: Post): Post
    suspend fun updatePost(post: Post): Post
    suspend fun deletePost(post: Post): Post
}