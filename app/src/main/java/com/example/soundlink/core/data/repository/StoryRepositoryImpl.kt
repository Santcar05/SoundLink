package com.example.soundlink.core.data.repository

import com.example.soundlink.core.data.datasource.retrofitspringboot.StoryApi
import com.example.soundlink.core.domain.model.Story
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.StoryRepository

class StoryRepositoryImpl(private val api: StoryApi): StoryRepository {
    override suspend fun getAllStories(): List<Story> {
        return api.getAllStories()
    }
    override suspend fun getStoryById(id: Long): Story {
        return api.getStoryById(id)
    }
    override suspend fun createStory(story: Story): Story {
        return api.createStory(story)
    }
    override suspend fun updateStory(story: Story): Story {
        return api.updateStory(story.id, story)
    }
    override suspend fun deleteStory(story: Story): Story {
        return api.deleteStory(story.id)
    }
}