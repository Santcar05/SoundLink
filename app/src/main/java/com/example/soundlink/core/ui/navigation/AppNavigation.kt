package com.example.soundlink.core.ui.navigation

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
import com.example.soundlink.core.ui.components.NavbarSoundLink
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
import com.example.soundlink.features.discovery.ui.screens.DiscoveryScreen
import com.example.soundlink.features.feed.ui.screens.createpost.CreatePostViewModel
import com.example.soundlink.features.feed.ui.screens.feed.FeedScreen
import com.example.soundlink.features.feed.ui.screens.feed.FeedViewModel
import com.example.soundlink.features.growth.ui.screens.GrowthScreen
import com.example.soundlink.features.profile.ui.screens.profile.ProfileScreen
import com.example.soundlink.features.profile.ui.screens.profile.ProfileViewModel
import com.example.soundlink.features.sonicDna.ui.screens.SonicDnaScreen

sealed class routes(val route: String) {
    object Login : routes("login")
    object Register : routes("register")
    object Feed : routes("feed")
    object CreatePost : routes("create_post")
    object Discovery : routes("discovery")
    object Growth : routes("growth")
    object SonicDna : routes("sonic_dna")
    object activitiesMenu : routes("activities_menu")
    object activityDescription : routes("activity_description")
    object profile : routes("profile")
}

// NavBar tab indices
private const val TAB_HOME = 0
private const val TAB_DISCOVER = 1
private const val TAB_GROWTH = 2
private const val TAB_PROFILE = 3

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val sessionViewModel = remember {
        SessionViewModel(getUserUseCase = AppContainer.GetUserUseCase)
    }

    var selectedIndex by remember { mutableStateOf(TAB_HOME) }

    val navBar = @Composable {
        NavbarSoundLink(
            items = listOf(
                NavBarItemSoundLink(icon = R.drawable.home, label = "Home"),
                NavBarItemSoundLink(icon = R.drawable.explore, label = "Descubrir"),
                NavBarItemSoundLink(icon = R.drawable.musicnotes, label = "Crecer"),
                NavBarItemSoundLink(icon = R.drawable.user, label = "Perfil"),
            ),
            selectedIndex = selectedIndex,
            onItemSelected = { index ->
                selectedIndex = index
                when (index) {
                    TAB_HOME -> navController.navigate(routes.Feed.route)
                    TAB_DISCOVER -> navController.navigate(routes.Discovery.route)
                    TAB_GROWTH -> navController.navigate(routes.Growth.route)
                    TAB_PROFILE -> navController.navigate(routes.profile.route)
                }
            }
        )
    }

    NavHost(navController = navController, startDestination = routes.Login.route) {

        composable(routes.Login.route) {
            val loginViewModel = remember {
                LoginViewModel(
                    loginUseCase = AppContainer.LoginUseCase,
                    getCurrentUser = AppContainer.GetUserUseCase
                )
            }
            LoginScreen(
                loginViewModel = loginViewModel,
                sessionViewModel = sessionViewModel,
                onRegisterClick = { navController.navigate(routes.Register.route) },
                onLoginClick = {
                    selectedIndex = TAB_HOME
                    navController.navigate(routes.Feed.route)
                },
                onForgotPasswordClick = { }
            )
        }

        composable(routes.Register.route) {
            val registerViewModel = remember {
                RegisterViewModel(
                    registerUseCase = AppContainer.RegisterUseCase,
                    sessionViewModel = sessionViewModel
                )
            }
            RegisterScreen(
                registerViewModel = registerViewModel,
                sessionViewModel = sessionViewModel,
                onLoginClick = { navController.navigate(routes.Login.route) },
                onRegisterClick = { _, _, _, _ ->
                    selectedIndex = TAB_HOME
                    navController.navigate(routes.Feed.route)
                }
            )
        }

        composable(routes.Feed.route) {
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
            FeedScreen(
                feedViewModel = feedViewModel,
                sessionViewModel = sessionViewModel,
                onFabClick = { navController.navigate(routes.CreatePost.route) },
                navBar = navBar
            )
        }

        composable(routes.CreatePost.route) {
            val createPostViewModel = remember {
                CreatePostViewModel(
                    createPostUseCase = AppContainer.CreatePostUseCase,
                    sessionViewModel = sessionViewModel
                )
            }
            CreatePostScreen(
                createPostViewModel = createPostViewModel,
                onPost = { navController.navigate(routes.Feed.route) },
                onCancel = { navController.navigate(routes.Feed.route) }
            )
        }

        composable(routes.Discovery.route) {
            DiscoveryScreen(navBar = navBar)
        }

        composable(routes.Growth.route) {
            GrowthScreen(
                onActivitiesClick = { navController.navigate(routes.activitiesMenu.route) },
                onDnaClick = { navController.navigate(routes.SonicDna.route) },
                navBar = navBar
            )
        }

        composable(routes.SonicDna.route) {
            SonicDnaScreen(
                onComplete = { _ ->
                    navController.navigate(routes.profile.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

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
                    NavBarItemSoundLink(R.drawable.explore, "Descubrir"),
                    NavBarItemSoundLink(R.drawable.musicnotes, "Crecer"),
                    NavBarItemSoundLink(R.drawable.user, "Perfil"),
                ),
                navSelectedIndex = TAB_GROWTH,
                onNavSelected = {},
                onActivityClick = { navController.navigate(routes.activityDescription.route) }
            )
        }

        composable(routes.activityDescription.route) {
            val fakeActivity = ActivityDetail(
                imageRes = R.drawable.activity1,
                name = "Relajación Sonora",
                description = "Sumérgete en una experiencia de relajación profunda utilizando sonidos suaves y de la naturaleza. Perfecto para disminuir el estrés y mejorar tu bienestar emocional.",
                rules = listOf(
                    "Encuentra un lugar cómodo",
                    "Usa auriculares para mejor calidad",
                    "Cierra los ojos y concéntrate en la respiración"
                ),
                prizes = listOf(
                    "Medalla de relajación",
                    "Reconocimiento en el perfil",
                    "Acceso a actividades exclusivas"
                )
            )
            var selectedSection by rememberSaveable { mutableStateOf(ActivityDetailSection.General) }
            ActivityDescriptionScreen(
                state = ActivityDescriptionState(
                    activity = fakeActivity,
                    selectedSection = selectedSection
                ),
                onSectionSelected = { selectedSection = it }
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
                onDnaClick = { navController.navigate(routes.SonicDna.route) }
            )
        }
    }
}
