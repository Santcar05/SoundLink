package com.example.soundlink.app.di

import com.example.soundlink.core.data.datasource.retrofitspringboot.PostApi
import com.example.soundlink.core.data.datasource.retrofitspringboot.RetrofitClient
import com.example.soundlink.core.data.datasource.retrofitspringboot.StoryApi
import com.example.soundlink.core.data.datasource.retrofitspringboot.UserApi
import com.example.soundlink.core.data.repository.PostRepositoryImpl
import com.example.soundlink.core.data.repository.StoryRepositoryImpl
import com.example.soundlink.core.data.repository.UserRepositoryImpl
import com.example.soundlink.core.domain.repository.PostRepository
import com.example.soundlink.core.domain.repository.StoryRepository
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.domain.usecases.GetAllPostsUseCase
import com.example.soundlink.core.domain.usecases.GetAllStoriesUseCase
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.features.auth.usecases.LoginUseCase
import com.example.soundlink.features.auth.usecases.RegisterUseCase

object AppContainer {

    // APIS (Data Sources)
    private val userApi: UserApi = RetrofitClient.userApi
    private val storyApi: StoryApi = RetrofitClient.storyApi
    private val postApi: PostApi = RetrofitClient.postApi

    val userRepository: UserRepository = UserRepositoryImpl(userApi)
    val storyRepository: StoryRepository = StoryRepositoryImpl(storyApi)
    val postRepository: PostRepository = PostRepositoryImpl(postApi)

    val LoginUseCase = LoginUseCase(userRepository)
    val RegisterUseCase = RegisterUseCase(userRepository)
    val GetUserUseCase = GetUserUseCase(userRepository)

    val GetAllStoriesUseCase = GetAllStoriesUseCase(storyRepository)

    val GetAllPostsUseCase = GetAllPostsUseCase(postRepository)



}