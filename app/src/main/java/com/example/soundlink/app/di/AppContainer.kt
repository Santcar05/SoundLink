package com.example.soundlink.app.di

import android.util.Log
import com.example.soundlink.core.data.datasource.retrofitspringboot.PostApi
import com.example.soundlink.core.data.datasource.retrofitspringboot.RetrofitClient
import com.example.soundlink.core.data.datasource.retrofitspringboot.StoryApi
import com.example.soundlink.core.data.datasource.retrofitspringboot.UserApi
import com.example.soundlink.core.data.repository.FeedWebSocketRepositoryImpl
import com.example.soundlink.core.data.repository.PostRepositoryImpl
import com.example.soundlink.core.data.repository.StoryRepositoryImpl
import com.example.soundlink.core.data.repository.UserRepositoryImpl
import com.example.soundlink.core.data.websockets.StompClient
import com.example.soundlink.core.data.websockets.WebSocketDataSource
import com.example.soundlink.core.domain.repository.FeedWebSocketRepository
import com.example.soundlink.core.domain.repository.PostRepository
import com.example.soundlink.core.domain.repository.StoryRepository
import com.example.soundlink.core.domain.repository.UserRepository
import com.example.soundlink.core.domain.usecases.ConnectWebSocketUseCase
import com.example.soundlink.core.domain.usecases.DisconnectWebSocketUseCase
import com.example.soundlink.core.domain.usecases.GetAllPostsUseCase
import com.example.soundlink.core.domain.usecases.GetAllStoriesUseCase
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.core.domain.usecases.ObserveNewPostsUseCase
import com.example.soundlink.features.auth.usecases.LoginUseCase
import com.example.soundlink.features.auth.usecases.RegisterUseCase
import com.example.soundlink.features.feed.usecases.CreatePostUseCase
import com.example.soundlink.features.profile.usecases.UpdateUserUseCase
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

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

    val CreatePostUseCase = CreatePostUseCase(postRepository)


    // WEB SOCKETS----
    // Configuración base
    private const val WS_URL = "ws://192.168.1.224:8080/ws"
    // Para emular usa: ""ws://10.0.2.2:8080/ws" "http://192.168.1.224:8080"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit. SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit. SECONDS)
        .pingInterval(20, TimeUnit.SECONDS)
        . addInterceptor { chain ->  // ✅ Logger para debug
            val request = chain. request()
            Log.d("OkHttp", "Request: ${request.url}")
            chain.proceed(request)
        }
        .build()

    private val stompClient = StompClient(WS_URL, okHttpClient)

    private val webSocketDataSource = WebSocketDataSource(stompClient, json)

    val feedWebSocketRepository: FeedWebSocketRepository =
        FeedWebSocketRepositoryImpl(webSocketDataSource)

    // Use Cases WebSocket
    val ConnectWebSocketUseCase = ConnectWebSocketUseCase(feedWebSocketRepository)
    val DisconnectWebSocketUseCase = DisconnectWebSocketUseCase(feedWebSocketRepository)
    val ObserveNewPostsUseCase = ObserveNewPostsUseCase(feedWebSocketRepository)
    val UpdateUserUseCase = UpdateUserUseCase(userRepository)
}