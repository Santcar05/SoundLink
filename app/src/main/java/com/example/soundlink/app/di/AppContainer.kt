package com.example.soundlink.app.di

import com.example.soundlink.app.di.AppContainer.GetUserUseCase
import com.example.soundlink.core.data.datasource.InMemoryDataSource
import com.example.soundlink.core.data.datasource.retrofitspringboot.RetrofitClient
import com.example.soundlink.core.data.datasource.retrofitspringboot.UserApi
import com.example.soundlink.core.data.repository.UserRepositoryImpl
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.features.auth.domain.usecases.LoginUseCase
import com.example.soundlink.features.auth.domain.usecases.RegisterUseCase

object AppContainer {

    val dataSource: UserApi = RetrofitClient.api

    val userRepository: UserRepository = UserRepositoryImpl(dataSource)

    val LoginUseCase = LoginUseCase(userRepository)
    val RegisterUseCase = RegisterUseCase(userRepository)
    val GetUserUseCase = GetUserUseCase(userRepository)



}