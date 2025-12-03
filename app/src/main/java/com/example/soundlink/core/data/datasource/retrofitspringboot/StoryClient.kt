package com.example.soundlink.core.data.datasource.retrofitspringboot

import com.example.soundlink.core.domain.model.Story
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StoryApi {

    @GET("/api/stories/")
    suspend fun getAllStories(): List<Story>

    @GET("/api/stories/{id}")
    suspend fun getStoryById(@Path("id") id: Long): Story

    @POST("/api/stories/")
    suspend fun createStory(@Body story: Story): Story

    @PUT("/api/stories/{id}")
    suspend fun updateStory(@Path("id") id: Long, @Body story: Story): Story

    @DELETE("/api/stories/{id}")
    suspend fun deleteStory(@Path("id") id: Long): Story

}