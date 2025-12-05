package com.example.soundlink.core.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.feature.createpost.CreatePostScreen
import com.example.soundlink.features.auth.ui.screens.login.LoginScreen
import com.example.soundlink.features.auth.ui.screens.login.LoginViewModel
import com.example.soundlink.features.auth.ui.screens.register.RegisterScreen
import com.example.soundlink.features.auth.ui.screens.register.RegisterViewModel
import com.example.soundlink.features.feed.ui.screens.createpost.CreatePostViewModel
import com.example.soundlink.features.feed.ui.screens.feed.FeedScreen
import com.example.soundlink.features.feed.ui.screens.feed.FeedViewModel

sealed class routes(val route: String){
    object Login : routes("login")
    object Register : routes("register")
    object Home : routes("home")

    object Feed : routes("feed")

    object CreatePost : routes("create_post")
}

@Composable
fun AppNavigation() {
    // Navigation Controller uses rememberNavController() to create a navigation controller
    val navController = rememberNavController()



    // Viewmodel Creations

    //Session Viewmodel GLOBAL
    val sessionViewModel = remember {
        SessionViewModel(
            getUserUseCase = AppContainer.GetUserUseCase
        )
    }


    //Login Viewmodel LOCAL
    val loginViewModel = remember {
        LoginViewModel(
            loginUseCase = AppContainer.LoginUseCase,
            getCurrentUser = AppContainer.GetUserUseCase
        )
    }

    val registerViewModel = remember {
        RegisterViewModel(
            registerUseCase = AppContainer.RegisterUseCase,
            sessionViewModel = sessionViewModel
        )
    }

    // Feed Viewmodel LOCAL
    val feedViewModel = remember {
        FeedViewModel(
            getAllPostsUseCase = AppContainer.GetAllPostsUseCase,
            getAllStoriesUseCase = AppContainer.GetAllStoriesUseCase,
            sessionViewModel = sessionViewModel,
            connectWebSocketUseCase = AppContainer.ConnectWebSocketUseCase,
            disconnectWebSocketUseCase = AppContainer.DisconnectWebSocketUseCase,
            observeNewPostsUseCase = AppContainer.ObserveNewPostsUseCase
        )
    }

    // CreatePost Viewmodel LOCAL
    val createPostViewModel = remember {
        CreatePostViewModel(
            createPostUseCase = AppContainer.CreatePostUseCase,
            sessionViewModel = sessionViewModel
        )
    }


    // Navigation Graph
    NavHost(navController = navController, startDestination = routes.Login.route) {
        composable(routes.Login.route) { LoginScreen(loginViewModel = loginViewModel,
            sessionViewModel = sessionViewModel,
            onRegisterClick = {
                navController.navigate(routes.Register.route)
            },
            onLoginClick = {
                navController.navigate(routes.Feed.route)
            }
            ) }
        composable(routes.Register.route) { RegisterScreen(registerViewModel = registerViewModel,
            sessionViewModel = sessionViewModel,
            onLoginClick = {
                navController.navigate(routes.Login.route)
            },
            onRegisterClick = { name, email, pass, age ->
                navController.navigate(routes.Feed.route)
            },
            ) }

        composable(routes.Feed.route) { FeedScreen(feedViewModel = feedViewModel, sessionViewModel = sessionViewModel,
            onFabClick = {
                navController.navigate(routes.CreatePost.route)
            }) }


        composable(routes.CreatePost.route) { CreatePostScreen(createPostViewModel = createPostViewModel, onPost = {

            navController.navigate(routes.Feed.route)

        }, onCancel = {
            navController.navigate(routes.Feed.route)
        }) }
    }


}
