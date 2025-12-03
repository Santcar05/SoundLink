package com.example.soundlink.core.domain.repository

import com.example.soundlink.core.domain.model.Story
import com.example.soundlink.core.data.datasource.retrofitspringboot.StoryApi

interface StoryRepository {

    suspend fun getAllStories(): List<Story>

    suspend fun getStoryById(id: Long): Story

    suspend fun createStory(story: Story): Story

    suspend fun updateStory(story: Story): Story

    suspend fun deleteStory(story: Story): Story
}