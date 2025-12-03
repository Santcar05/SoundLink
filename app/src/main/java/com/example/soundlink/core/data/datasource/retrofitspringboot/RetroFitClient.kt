package com.example.soundlink.core.data.datasource.retrofitspringboot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val BASE_URL = "http://192.168.1.224:8080"

    // Usa 10.0.2.2 if you are using Android Studio Emulator

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
    val storyApi: StoryApi by lazy { retrofit.create(StoryApi::class.java) }
    val postApi: PostApi by lazy { retrofit.create(PostApi::class.java) }

}
