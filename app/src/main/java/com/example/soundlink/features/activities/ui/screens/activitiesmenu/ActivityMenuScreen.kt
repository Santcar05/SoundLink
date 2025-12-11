package com.example.soundlink.features.activities.ui.screens.activitiesmenu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.soundlink.R
import com.example.soundlink.core.ui.components.NavBarItemSoundLink
import com.example.soundlink.core.ui.components.NavbarSoundLink
import com.example.soundlink.app.theme.SoundLinkTheme
import kotlinx.coroutines.delay

data class ActivityItem(
    val imageRes: Int,
    val name: String,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ActivityMenuScreen(
    activities: List<ActivityItem>,
    navBarItems: List<NavBarItemSoundLink>,
    navSelectedIndex: Int,
    onNavSelected: (Int) -> Unit,
    onActivityClick: (ActivityItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf(navSelectedIndex) }
    val transition = rememberInfiniteTransition(label = "BGGradient")
    val bgShift by transition.animateFloat(
        initialValue = 0f, targetValue = 3000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GradientShift"
    )
    Scaffold(
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // Used to prevent the navbar from being hidden by the bottom navigation bar
            ) {
                NavbarSoundLink(
                    items = listOf(
                        NavBarItemSoundLink(icon = R.drawable.home, label = "Home"),
                        NavBarItemSoundLink(icon = R.drawable.musicnotes, label = "Activities"),
                        NavBarItemSoundLink(icon = R.drawable.search, label = "Search"),
                        NavBarItemSoundLink(icon = R.drawable.user, label = "Profile"),

                        ),
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it },
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.29f),
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.70f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.31f)
                        ),
                        start = Offset(0f, bgShift),
                        end = Offset(1000f, 1000f - bgShift)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                // Header
                Spacer(Modifier.height(32.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(450)) + slideInVertically(tween(450)),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Actividades SoundLink",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Box(
                            modifier = Modifier
                                .heightIn(min = 30.dp)
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.17f),
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                "Aquí puedes explorar y elegir entre las actividades disponibles. ¡Descubre, escucha y disfruta con SoundLink!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Activities List
                AnimatedContent(
                    targetState = activities,
                    transitionSpec = {
                        fadeIn(tween(900)) + slideInVertically(tween(900)) togetherWith fadeOut(tween(600))
                    },
                    label = "Activities-Anim"
                ) { items ->
                    Box(
                        modifier = Modifier.weight(1f, fill = false), // main content
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (items.isNotEmpty()) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(22.dp),
                                horizontalArrangement = Arrangement.spacedBy(18.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                items(items) { activity ->
                                    AnimatedActivityCard(
                                        activity = activity,
                                        onClick = { onActivityClick(activity) },
                                    )
                                }
                            }
                        } else {
                            Text(
                                "No hay actividades disponibles.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 60.dp)
                            )
                        }
                    }
                }

                // Spacer and NavBar
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedActivityCard(
    activity: ActivityItem,
    onClick: () -> Unit
) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay((100..350).random().toLong()) // staggered entrance
        show = true
    }
    AnimatedVisibility(
        visible = show,
        enter = scaleIn(
            initialScale = 0.85f,
            animationSpec = tween(520, easing = LinearOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(520)),
        exit = fadeOut(),
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            tonalElevation = 4.dp,
            modifier = Modifier
                .aspectRatio(1.08f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(22.dp)
                )
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Imagen principal
                Image(
                    painter = painterResource(id = activity.imageRes),
                    contentDescription = activity.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    alpha = 0.89f
                )
                // Gradiente inferior sobre la imagen
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.69f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.90f)
                                )
                            ),
                        )
                )
                // Título de la actividad
                Text(
                    text = activity.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 9.dp, top = 10.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivitiesScreenPreview() {
    SoundLinkTheme {
        ActivityMenuScreen(
            activities = listOf(
                ActivityItem(R.drawable.logo, "Relajación"),
                ActivityItem(R.drawable.logo, "Concentración"),
                ActivityItem(R.drawable.logo, "Ejercicio"),
                ActivityItem(R.drawable.logo, "Creatividad"),
            ),
            navBarItems = listOf(
                NavBarItemSoundLink(R.drawable.home, "Inicio"),
                NavBarItemSoundLink(R.drawable.search, "Buscar"),
                NavBarItemSoundLink(R.drawable.user, "Perfil"),
            ),
            navSelectedIndex = 0,
            onNavSelected = {},
            onActivityClick = {},
        )
    }
}