package com.example.soundlink.core.ui.components

import android.media.SoundPool
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundlink.R


@Composable
fun ClickButtonWithSound() {
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

    // Botón ultra simple
    Text(
        text = "Click con sonido",
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            }
    )

    // Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClickButtonPreview() {
    ClickButtonWithSound()
}
