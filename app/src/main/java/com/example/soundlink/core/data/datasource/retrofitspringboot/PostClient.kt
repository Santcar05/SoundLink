package com.example.soundlink.core.data.datasource.retrofitspringboot

import com.example.soundlink.core.data.dto.CreatePostRequest
import com.example.soundlink.core.domain.model.Post
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostApi {

    @GET("/api/posts/")
    suspend fun getAllPosts(): List<Post>

    @GET("/api/posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Post

    @POST("/api/posts/")
    suspend fun createPost(@Body post: CreatePostRequest): CreatePostRequest

    @PUT("/api/posts/{id}")
    suspend fun updatePost(@Path("id") id: Long, @Body post: Post): Post

    @DELETE("/api/posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Post

}