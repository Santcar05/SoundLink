package com.example.soundlink.core.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    avatar: Painter,
    username: String,
    verified: Boolean = false,
    genre: String = "Synthwave",
    time: String = "Hace 2 horas",
    title: String,
    description: String,
    tags: List<String>,
    likes: String,
    comments: String,
    shares: String,
    onPlayClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(20.dp)

    // ------------ Animación de elevación ----------------
    var pressed by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (pressed) 18.dp else 8.dp,
        animationSpec = tween(300)
    )

    // ------------ Fondo animado (gradient shift) --------
    val infinite = rememberInfiniteTransition()
    val gradientShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.55f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.30f)
    )


    Card(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        modifier = modifier
            .fillMaxWidth()

            .clickable { pressed = !pressed }
    ) {

        // -------- HEADER ------------------------
        Row(
            Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .clickable(onClick = {//TODO
                    })
                    .size(46.dp)
                    .clip(CircleShape)
            )

            Column(
                Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(username, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    if (verified) {
                        Icon(
                            painterResource(id = R.drawable.verified), // is_verified
                            contentDescription = "verified",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp).padding(start = 4.dp)
                        )
                    }
                }

                Text(time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                AssistChip(
                    onClick = {},
                    label = { Text(genre) },
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            IconButton(onClick = onMoreClick) {
                Icon(
                    painterResource(id = R.drawable.more
                    ), // ic_more
                    contentDescription = "Mas opciones",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // -------- MEDIA PLAYER AREA -------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.linearGradient(
                        colors = backgroundColors,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(gradientShift, gradientShift)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            // ---- ANIMACIÓN DEL BOTÓN PLAY ------
            val pulse by infinite.animateFloat(
                initialValue = 0.88f,
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(900, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                        shadowElevation = 22f
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
                    .clickable { onPlayClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo), // ic_play
                    contentDescription = "play",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        // -------- TEXTO  ------------------------
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium,color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(6.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)

            Spacer(Modifier.height(12.dp))

            Row {
                tags.forEach {
                    Text(
                        text = "#$it",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // -------- FOOTER (likes / comments / share) ------
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {

            // Like
            Row(
                modifier = Modifier.clickable(onClick = {//TODO
                    }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(id = R.drawable.like), contentDescription = null, // ic_like
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(6.dp))
                Text(likes, color = MaterialTheme.colorScheme.onBackground)
            }

            // Comments
            Row(
                modifier = Modifier.clickable(onClick = {//TODO
                    }),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = R.drawable.comment), contentDescription = null, modifier = Modifier

                    .size(24.dp), tint = MaterialTheme.colorScheme.onBackground) // ic_comment
                Spacer(Modifier.width(6.dp))
                Text(comments, color = MaterialTheme.colorScheme.onBackground)
            }

            // Shares
            Row(
                modifier = Modifier.clickable(onClick = {//TODO
                    }),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(id = R.drawable.share), contentDescription = null, modifier = Modifier

                    .size(24.dp), tint = MaterialTheme.colorScheme.onBackground) // ic_share
                Spacer(Modifier.width(6.dp))
                Text(shares, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostCard() {
    SoundLinkTheme {
        // Dark theme

        PostCard(
            avatar = painterResource(R.drawable.user),
            username = "The Neons",
            verified = true,
            title = "Midnight Drive - Nuevo Single",
            description = "Nuestro nuevo single ya está disponible 🎵 Producido con sintetizadores vintage y beats modernos.",
            tags = listOf("synthwave", "newmusic", "electronic"),
            likes = "1.2K",
            comments = "234",
            shares = "89",
        )
    }
}
@Composable
fun AnimatedLoadingBorder(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 3.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val infinite = rememberInfiniteTransition()

    // Este valor va de 0f a 1f → desplazaremos los colorsStops en ese rango
    val shift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Colores del borde (puedes cambiarlos)
    val baseColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primary
    )

    // --- Mover los colores sin rotar el borde ---
    val dynamicStops = remember(baseColors, shift) {
        val size = baseColors.size
        val shiftedStops = (0 until size).associate { index ->
            // posición desplazada circularmente
            val pos = ((index / size.toFloat()) + shift) % 1f
            pos to baseColors[index]
        }
        shiftedStops.toList().sortedBy { it.first }.toTypedArray()
    }

    Box(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .drawBehind {

            val brush = Brush.sweepGradient(
                *dynamicStops,
                center = center
            )

            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(
                    shape.topStart.toPx(size, this),
                    shape.topStart.toPx(size, this)
                ),
                style = Stroke(borderWidth.toPx())
            )
        }.padding(borderWidth),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostCard2() {
    SoundLinkTheme {
        AnimatedLoadingBorder(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            borderWidth = 3.dp
        ) {
            PostCard(
                avatar = painterResource(R.drawable.user),
                username = "The Neons",
                verified = true,
                title = "Midnight Drive - Nuevo Single",
                description = "Nuestro nuevo single ya está disponible 🎵 Producido con sintetizadores vintage y beats modernos.",
                tags = listOf("synthwave", "newmusic", "electronic"),
                likes = "1.2K",
                comments = "234",
                shares = "89",
            )
        }
    }
}
