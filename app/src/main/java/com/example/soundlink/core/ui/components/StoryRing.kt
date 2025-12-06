package com.example.soundlink.core.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.SoundPool
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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
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
    image: Any,
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    ringWidth: Dp = 8.dp,
    isSeen: Boolean = false,
    onClick: () -> Unit = {}
) {

    val context = LocalContext.current

    // Crear SoundPool(Soundpool es la clase que se encarga de cargar los sonidos)
/*
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }


    // Cargar sonido
    val soundId = remember {
        soundPool.load(context, R.raw.click, 1)
    }

 */


    // Used to load the image
    val imagePainter = universalPainter(image)


    val infinite = rememberInfiniteTransition()

    // Solo este valor rotará el degradado del anillo
    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Colores del anillo
    val ringColors = if (isSeen) {
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.primary
        )
    }

    Box(
        modifier = modifier

            .size(size),
        contentAlignment = Alignment.Center
    ) {
        val ringStrokePx = with(LocalDensity.current) { ringWidth.toPx() }

        // Dibujamos el ring y rotamos SOLO SU BRUSH
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    val radius = size.toPx() / 2f

                    // Aquí es donde ocurre la rotación del color
                    val sweepBrush = Brush.sweepGradient(
                        colors = ringColors
                    )

                    withTransform({
                        rotate(rotation, pivot = Offset(radius, radius))
                    }) {
                        drawCircle(
                            brush = sweepBrush,
                            radius = radius - ringStrokePx / 2f,
                            style = Stroke(width = ringStrokePx)
                        )
                    }
                }
        )

        // Imagen circular (NO se rota)
        Box(
            modifier = Modifier
                .size(size - ringWidth * 2)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                   // soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
                    onClick() }
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

    // Liberar recursos al salir
   /* DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }

    */
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
        //val painter = painterResource(id = R.drawable.user) // crea este drawable para probar
        //URL
        val url = "https://es.wikipedia.org/wiki/Julian_Casablancas"
        Box(
            modifier = Modifier
                .padding(24.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            //Send an URL
            StoryRing(
                image = url, //painter,
                size = 96.dp,
                ringWidth = 9.dp,
                isSeen = false,
                onClick = { /* abrir story */ }
            )
        }
    }
}


@Composable
fun universalPainter(image: Any): Painter {
    return when (image) {

        // URL -> Coil
        is String -> {
            rememberAsyncImagePainter(
                model = image,
                fallback = painterResource(id = R.drawable.user),
                error = painterResource(id = R.drawable.user)
            )
        }

        // Painter
        is Painter -> image

        // Bitmap
        is Bitmap -> BitmapPainter(image.asImageBitmap())

        // ImageBitmap
        is ImageBitmap -> BitmapPainter(image)

        // Drawable resource ID
        is Int -> painterResource(id = image)

        // Tipo desconocido -> fallback
        else -> painterResource(id = R.drawable.user)
    }
}
