package com.example.soundlink.core.ui.components

import android.media.SoundPool
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme

@Composable
fun NavbarSoundLink(
    items: List<NavBarItemSoundLink>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }
    val soundId = remember {
        soundPool.load(context, R.raw.click, 1)
    }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val indicatorColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp)
            .clip(MaterialTheme.shapes.large),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            NavBarButton(
                item = item,
                selected = index == selectedIndex,
                indicatorColor = indicatorColor,
                onClick = {
                    soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
                    onItemSelected(index) }
            )
        }
    }
}

@Composable
fun NavBarButton(
    item: NavBarItemSoundLink,
    selected: Boolean,
    indicatorColor: Color,
    onClick: () -> Unit
) {
    val transition = updateTransition(selected, label = "NavBarButtonTransition")
    val context = LocalContext.current
    // Crear SoundPool(Soundpool es la clase que se encarga de cargar los sonidos)
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }


    // Cargar sonido
    val soundId = remember {
        soundPool.load(context, R.raw.click, 1)
    }

    val iconScale by transition.animateFloat(
        label = "IconScale",
        transitionSpec = { tween(durationMillis = 300, easing = FastOutSlowInEasing) }
    ) { sel -> if (sel) 1.3f else 1f }

    val fontWeight by transition.animateFloat(
        label = "FontWeight",
        transitionSpec = { tween(durationMillis = 250) }
    ) { sel -> if (sel) 900f else 400f }

    val indicatorAlpha by transition.animateFloat(
        label = "IndicatorAlpha",
        transitionSpec = { tween(durationMillis = 350) }
    ) { sel -> if (sel) 1f else 0f }

    Box(
        modifier = Modifier

            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .semantics { contentDescription = item.label },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.label,
                tint = if (selected) indicatorColor else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier

                    .size(32.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    }
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.label,
                color = if (selected) indicatorColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontWeight = if (fontWeight > 700) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp
            )
        }
        // Indicador animado
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -8 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -8 })
        ) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 12.dp)
                    .size(22.dp, 4.dp)
                    .clip(CircleShape)
                    .background(indicatorColor.copy(alpha = indicatorAlpha))
            )
        }
    }
}

// Modelo de item para la navBar
data class NavBarItemSoundLink(
    val icon: Int, // Resource ID
    val label: String,
)

@Preview
@Composable
fun NavBarPreview() {
    SoundLinkTheme {
        NavbarSoundLink(
            items = listOf(
                NavBarItemSoundLink(icon = R.drawable.home, label = "Home"),
                NavBarItemSoundLink(icon = R.drawable.search, label = "Search"),
                NavBarItemSoundLink(icon = R.drawable.user, label = "Profile"),
            ),
            selectedIndex = 0,
            onItemSelected = { },
        )
    }
}