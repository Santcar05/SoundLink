package com.example.soundlink.core.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.soundlink.R
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.core.ui.components.NavBarItemSoundLink
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.feature.createpost.CreatePostScreen
import com.example.soundlink.features.activities.ui.screens.activitiesmenu.ActivityItem
import com.example.soundlink.features.activities.ui.screens.activitiesmenu.ActivityMenuScreen
import com.example.soundlink.features.activities.ui.screens.activitydescription.ActivityDescriptionScreen
import com.example.soundlink.features.activities.ui.screens.activitydescription.ActivityDescriptionState
import com.example.soundlink.features.activities.ui.screens.activitydescription.ActivityDetail
import com.example.soundlink.features.activities.ui.screens.activitydescription.ActivityDetailSection
import com.example.soundlink.features.auth.ui.screens.login.LoginScreen
import com.example.soundlink.features.auth.ui.screens.login.LoginViewModel
import com.example.soundlink.features.auth.ui.screens.register.RegisterScreen
import com.example.soundlink.features.auth.ui.screens.register.RegisterViewModel
import com.example.soundlink.features.feed.ui.screens.createpost.CreatePostViewModel
import com.example.soundlink.features.feed.ui.screens.feed.FeedScreen
import com.example.soundlink.features.feed.ui.screens.feed.FeedViewModel
import com.example.soundlink.features.profile.ui.screens.profile.ProfileScreen
import com.example.soundlink.features.profile.ui.screens.profile.ProfileViewModel
import androidx.compose.runtime.collectAsState
import com.example.soundlink.core.ui.components.NavbarSoundLink

sealed class routes(val route: String){
    object Login : routes("login")
    object Register : routes("register")
    object Home : routes("home")

    object Feed : routes("feed")

    object CreatePost : routes("create_post")

    object activitiesMenu : routes("activities_menu")

    object activityDescription : routes("activity_description")

    object profile : routes("profile")
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




    var selectedIndex by remember { mutableStateOf(0) }

    //NavBar creation
    val navBar = @Composable {
        NavbarSoundLink(
            items = listOf(
                NavBarItemSoundLink(icon = R.drawable.home, label = "Home"),
                NavBarItemSoundLink(icon = R.drawable.musicnotes, label = "Activities"),
                NavBarItemSoundLink(icon = R.drawable.search, label = "Search"),
                NavBarItemSoundLink(icon = R.drawable.user, label = "Profile"),

                ),
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it
                if(selectedIndex == 0){
                    navController.navigate(routes.Feed.route)
                }else if(selectedIndex == 1){

                    navController.navigate(routes.activitiesMenu.route)
                }else if(selectedIndex == 3){
                    navController.navigate(routes.profile.route)
                }
                             },
        )
    }




    // Navigation Graph
    NavHost(navController = navController, startDestination = routes.Login.route) {


        composable(routes.Login.route) {
            // Viewmodel creation
            //Login Viewmodel LOCAL
            val loginViewModel = remember {
                LoginViewModel(
                    loginUseCase = AppContainer.LoginUseCase,
                    getCurrentUser = AppContainer.GetUserUseCase
                )
            }

            LoginScreen(loginViewModel = loginViewModel,
            sessionViewModel = sessionViewModel,
            onRegisterClick = {
                navController.navigate(routes.Register.route)
            },
            onLoginClick = {
                navController.navigate(routes.Feed.route)
            },
                onForgotPasswordClick = {
                    navController.navigate(routes.activitiesMenu.route)
                }
            ) }
        composable(routes.Register.route) {
            val registerViewModel = remember {
                RegisterViewModel(
                    registerUseCase = AppContainer.RegisterUseCase,
                    sessionViewModel = sessionViewModel
                )
            }



            RegisterScreen(registerViewModel = registerViewModel,
            sessionViewModel = sessionViewModel,
            onLoginClick = {
                navController.navigate(routes.Login.route)
            },
            onRegisterClick = { name, email, pass, age ->
                navController.navigate(routes.Feed.route)
            },
            ) }

        composable(routes.Feed.route) {
            // Viewmodel creation
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


            FeedScreen(feedViewModel = feedViewModel, sessionViewModel = sessionViewModel,
            onFabClick = {
                navController.navigate(routes.CreatePost.route)
            },
                navBar = navBar

            ) }


        composable(routes.CreatePost.route) {
            // CreatePost Viewmodel LOCAL
            val createPostViewModel = remember {
                CreatePostViewModel(
                    createPostUseCase = AppContainer.CreatePostUseCase,
                    sessionViewModel = sessionViewModel
                )
            }


            CreatePostScreen(createPostViewModel = createPostViewModel, onPost = {

            navController.navigate(routes.Feed.route)

        }, onCancel = {
            navController.navigate(routes.Feed.route)
        }) }

        composable(routes.activitiesMenu.route) {

            ActivityMenuScreen(
                activities = listOf(
                    ActivityItem(R.drawable.activity1, "Relajación"),
                    ActivityItem(R.drawable.activity2, "Concentración"),
                    ActivityItem(R.drawable.activity3, "Ejercicio"),
                    ActivityItem(R.drawable.activity4, "Creatividad"),
                ),
                navBarItems = listOf(
                    NavBarItemSoundLink(R.drawable.home, "Inicio"),
                    NavBarItemSoundLink(R.drawable.search, "Buscar"),
                    NavBarItemSoundLink(R.drawable.user, "Perfil"),
                ),
                navSelectedIndex = 0,
                onNavSelected = {},
                onActivityClick = { activity ->
                    navController.navigate(routes.activityDescription.route)
                },
            )

        }
        composable(routes.activityDescription.route) {
            val fakeActivity = ActivityDetail(
                imageRes = R.drawable.activity1,
                name = "Relajación Sonora",
                description = "Sumérgete en una experiencia de relajación profunda utilizando sonidos suaves y de la naturaleza. Perfecto para disminuir el estrés y mejorar tu bienestar emocional.",
                rules = listOf("Encuentra un lugar cómodo", "Usa auriculares para mejor calidad", "Cierra los ojos y concéntrate en la respiración"),
                prizes = listOf("Medalla de relajación", "Reconocimiento en el perfil", "Acceso a actividades exclusivas")
            )
            var selectedSection by rememberSaveable { mutableStateOf(ActivityDetailSection.General) }
            val state = ActivityDescriptionState(
                activity = fakeActivity,
                selectedSection = selectedSection
            )
            ActivityDescriptionScreen(
                state = state,
                onSectionSelected = { section -> selectedSection = section },
            )

        }
        composable(routes.profile.route) {

            val profileViewModel = remember {
                ProfileViewModel(
                    sessionViewModel = sessionViewModel,
                    updateUserUseCase = AppContainer.UpdateUserUseCase
                )
            }

            ProfileScreen(
                viewModel = profileViewModel,
            )
        }
    }







}
