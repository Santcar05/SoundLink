package com.example.soundlink.core.data.datasource.retrofitspringboot

import com.example.soundlink.core.data.dto.LoginRequest
import com.example.soundlink.core.data.dto.RegisterRequest
import com.example.soundlink.core.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {


    @GET("/api/users/")
    suspend fun getAllUsers(): List<User>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): User

    @GET("/api/users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): User

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): User




}
