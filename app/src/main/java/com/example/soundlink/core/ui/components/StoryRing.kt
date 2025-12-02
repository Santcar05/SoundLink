package com.example.soundlink.core.ui.components

import android.graphics.Canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.soundlink.app.theme.SoundLinkTheme
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import com.example.soundlink.R


/**
 * StoryRing composable:
 * - Usa colores del MaterialTheme.colorScheme para generar un anillo degradado animado.
 * - Soporta estado "visto" (isSeen) para mostrar un anillo más apagado.
 * - Efectos: rotación suave del degradado, pulso sutil, brillo tipo shimmer.
 *
 * Parámetros:
 * @param imagePainter -> Painter para la imagen del usuario (avatar).
 * @param size -> diámetro total del componente (incluye anillo).
 * @param ringWidth -> grosor del anillo.
 * @param isSeen -> si true muestra estilo "visto" (menos llamativo).
 * @param onClick -> callback al tocar (opcional).
 */
@Composable
fun StoryRing(
    imagePainter: Painter,
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    ringWidth: Dp = 8.dp,
    isSeen: Boolean = false,
    onClick: () -> Unit = {}
) {
    // Extraemos colores desde el tema (asume que tu theme está definido).
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val background = MaterialTheme.colorScheme.background

    // Animaciones: rotación del anillo y pulso del avatar.
    val infinite = rememberInfiniteTransition()

    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSeen) 8000 else 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Shimmer offset (for subtle moving light on the ring)
    val shimmerOffset by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Si está visto, usamos una paleta apagada.
    val ringColors = if (isSeen) {
        listOf(primary.copy(alpha = 0.5f), secondary.copy(alpha = 0.25f))
    } else {
        // combinación llamativa desde el tema
        listOf(primary, secondary, tertiary, primary)
    }

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                // rotamos el anillo entero (la imagen será contrarotada dentro si quieres que no rote)
                rotationZ = rotation
                scaleX = pulse
                scaleY = pulse
                transformOrigin = TransformOrigin.Center
            }
            .padding(2.dp) // margen interno para la sombra visual
            .shadow(elevation = 8.dp, shape = CircleShape, clip = false),
        contentAlignment = Alignment.Center
    ) {
        // Outer gradient ring drawn with drawBehind for full control
        val ringStrokePx = with(LocalDensity.current) { ringWidth.toPx() }
        Box(
            Modifier
                .matchParentSize()
                .drawBehind {
                    val diameter = size.toPx()
                    val arcSize = Size(diameter, diameter)
                    val radius = diameter / 2f
                    // Brush tipo sweep (circular) para degradado del anillo
                    val sweepBrush = Brush.sweepGradient(
                        colors = ringColors,
                        center = Offset(radius, radius)
                    )
                    // dibuja anillo completo usando stroke
                    drawCircle(
                        brush = sweepBrush,
                        radius = radius - ringStrokePx / 2f,
                        style = Stroke(width = ringStrokePx)
                    )

                    // Shimmer: un pequeño overlay radial translúcido que se mueve
                    // (se simula con un arc semi-transparente)
                    val shimmerBrush = Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = if (isSeen) 0.03f else 0.10f), Color.Transparent),
                        center = Offset(radius + shimmerOffset * radius * 0.3f, radius - shimmerOffset * radius * 0.2f),
                        radius = radius * 0.9f
                    )
                    // dibuja encima del anillo (ligera máscara)
                    drawCircle(
                        brush = shimmerBrush,
                        radius = radius - ringStrokePx / 2f
                    )
                }
        )

        // Imagen circular (avatar)
        Box(
            modifier = Modifier
                .size(size - ringWidth * 2f) // interior dejando espacio para el anillo
                .clip(CircleShape)
                .background(background)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.06f)),
                    shape = CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        // Pequeño indicador "online" con glow (opcional; se superpone en la esquina inferior derecha)
        if (!isSeen) {
            val indicatorSize = (size.value * 0.20f).dp
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (indicatorSize * 0.1f), y = (indicatorSize * 0.1f))
                    .size(indicatorSize)
                    .shadow(elevation = 6.dp, shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    .border(2.dp, background, CircleShape)
            )
        }
    }
}

/** ----------------------------
 * Preview / ejemplo de uso
 * (Cambia R.drawable.avatar_example por tu recurso)
 * ---------------------------- */
@Preview(showBackground = true)
@Composable
fun PreviewStoryRing() {
    SoundLinkTheme {
        // Para la preview usamos un painter local. En tu app reemplaza con la imagen real del usuario (Coil, Glide, etc.)
        val painter = painterResource(id = R.drawable.user) // crea este drawable para probar
        Box(
            modifier = Modifier
                .padding(24.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            StoryRing(
                imagePainter = painter,
                size = 96.dp,
                ringWidth = 9.dp,
                isSeen = false,
                onClick = { /* abrir story */ }
            )
        }
    }
}