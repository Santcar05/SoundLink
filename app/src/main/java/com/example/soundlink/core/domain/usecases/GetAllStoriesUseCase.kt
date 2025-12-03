package com.example.soundlink.core.domain.usecases

import com.example.soundlink.core.domain.model.Story
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.domain.repository.StoryRepository

class GetAllStoriesUseCase(private val storyRepository: StoryRepository){
    suspend operator fun invoke(): List<Story> {
        return storyRepository.getAllStories()
    }
}