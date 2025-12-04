package com.example.soundlink.core.data.repository

import com.example.soundlink.core.data.datasource.retrofitspringboot.PostApi
import com.example.soundlink.core.data.dto.CreatePostRequest
import com.example.soundlink.core.domain.model.Post
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.PostRepository

class PostRepositoryImpl(private val postApi: PostApi): PostRepository {
    override suspend fun getAllPosts(): List<Post> {
        return postApi.getAllPosts()
        //Create 5 posts
        /*
        val posts = listOf(
            Post(1,
                User(1, "User 1", "user1@gmail.com", "password1", 25), "Post 1", "Description 1", emptyList(), 0, 0, 0, System.currentTimeMillis()),
            Post(2, User(2, "User 2", "user2@gmail.com", "password2", 30), "Post 2", "Description 2", emptyList(), 0, 0, 0, System.currentTimeMillis()),
            Post(3, User(3, "User 3", "user3@gmail.com", "password3", 35), "Post 3", "Description 3", emptyList(), 0, 0, 0, System.currentTimeMillis()),
            Post(4, User(4, "User 4", "user4@gmail.com", "password4", 40), "Post 4", "Description 4", emptyList(), 0, 0, 0, System.currentTimeMillis()),
            Post(5, User(5, "User 5", "user5@gmail.com", "password5", 45), "Post 5", "Description 5", emptyList(), 0, 0, 0, System.currentTimeMillis())
        )
        return posts

         */
    }
    override suspend fun getPostById(id: Long): Post {
        return postApi.getPostById(id)
    }
    override suspend fun createPost(post: CreatePostRequest): CreatePostRequest {
        return postApi.createPost(post)
    }
    override suspend fun updatePost(post: Post): Post {
        return postApi.updatePost(post.id, post)
    }
    override suspend fun deletePost(post: Post): Post {
        return postApi.deletePost(post.id)
    }
}