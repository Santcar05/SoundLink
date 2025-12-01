package com.example.soundlink.core.data.datasource

import com.example.soundlink.core.data.dto.LoginRequest
import com.example.soundlink.core.domain.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): User
}
