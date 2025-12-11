package com.example.soundlink.features.feed.ui.screens.feed

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.soundlink.R
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.data.websockets.StompClient
import com.example.soundlink.core.data.websockets.WebSocketDataSource
import com.example.soundlink.core.ui.components.AnimatedLoadingBorder
import com.example.soundlink.core.ui.components.Header
import com.example.soundlink.core.ui.components.NavBarItemSoundLink
import com.example.soundlink.core.ui.components.NavbarSoundLink
import com.example.soundlink.core.ui.components.PostCard
import com.example.soundlink.core.ui.components.StoryRing
import com.example.soundlink.core.ui.components.universalPainter
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.core.utils.timestampToLegible
import com.example.soundlink.features.auth.ui.screens.login.LoginState
import com.example.soundlink.features.feed.ui.components.FabButton
import kotlinx.serialization.json.Json

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    feedViewModel: FeedViewModel,
    onFabClick: () -> Unit,
    // NavBar composable
    navBar: @Composable () -> Unit,
) {
    val state = feedViewModel.uiState.collectAsState().value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FabButton(onClick = onFabClick)
        },
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // Used to prevent the navbar from being hidden by the bottom navigation bar
            ) {
                navBar()
            }
        }
    ){ contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .padding(4.dp)
                .fillMaxSize()
        ) {


            Column {
                Header()

                LazyRow(
                    modifier = modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    items(state.stories) { story ->
                        Column(modifier = modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            StoryRing(
                                image = story.user.avatarUrl,
                                size = 88.dp,
                                ringWidth = 8.dp,
                                isSeen = story.isSeen,
                                onClick = { }
                            )
                            Text(story.user.name, fontSize = 14.sp)
                            Log.d("FeedScreen", "Story: ${story.user.name}")
                        }

                    }
                }
            }

            //Posts
            LazyColumn {
                items(state.posts){ post ->
                    AnimatedLoadingBorder(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        borderWidth = 3.dp
                    ) {

                        Log.d("FeedScreen", "Post: ${post.title}")

                        PostCard(
                            modifier = modifier,
                            avatar = universalPainter(post.user.avatarUrl),
                            username = post.user.name,
                            verified = post.user.verified,
                            genre = "Rock", //TODO: Cambiar por el género del post
                            time = timestampToLegible(post.timestamp),
                            title = post.title,
                            description = post.description,
                            tags = post.tags,
                            likes = post.likes.toString(),
                            comments = post.comments.toString(),
                            shares = post.shares.toString(),
                            onPlayClick = { },
                            onMoreClick = { }
                        )
                    }

                }
            }



        }
    }
}


@Preview
@Composable
fun FeedScreenPreview() {

    val selectedIndex = remember { mutableStateOf(0) }
    SoundLinkTheme {
        FeedScreen(
            sessionViewModel = SessionViewModel(getUserUseCase = AppContainer.GetUserUseCase),
            feedViewModel = FeedViewModel(
                getAllStoriesUseCase = AppContainer.GetAllStoriesUseCase,
                getAllPostsUseCase = AppContainer.GetAllPostsUseCase,
                sessionViewModel = SessionViewModel(getUserUseCase = AppContainer.GetUserUseCase),
                connectWebSocketUseCase = AppContainer.ConnectWebSocketUseCase,
                disconnectWebSocketUseCase = AppContainer.DisconnectWebSocketUseCase,
                observeNewPostsUseCase = AppContainer.ObserveNewPostsUseCase
            ),
                onFabClick = {
                    val sum = 1 + 1
                    Log.d("FeedScreen", "Sum: $sum")
                },

            navBar = {
                NavbarSoundLink(
                    items = listOf(
                        NavBarItemSoundLink(icon = R.drawable.home, label = "Home"),
                        NavBarItemSoundLink(icon = R.drawable.musicnotes, label = "Activities"),
                        NavBarItemSoundLink(icon = R.drawable.search, label = "Search"),
                        NavBarItemSoundLink(icon = R.drawable.user, label = "Profile"),

                    ),
                    selectedIndex = 0,
                    onItemSelected = {

                                     },
                )
            }

        )
    }
}
