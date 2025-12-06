package com.example.soundlink.core.ui.components

import android.media.SoundPool
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme

@Composable
fun NeonButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, intensity: Float = 10f) {

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

    val glowIntensity by animateFloatAsState(
        targetValue = 20f,
        animationSpec = tween(durationMillis = 300),
        label = "glowAnimation"

    )
    Box(
        modifier = modifier

            .shadow(
                elevation = intensity.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            )
    ){

        //Button almost square
        Button(onClick = onClick, modifier = modifier.shadow(elevation = intensity.dp, shape = MaterialTheme.shapes.small)
            .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
            shape = RoundedCornerShape(8.dp)) {
            Text(text = text, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            )
        }

        //Second Button layer
        Button(onClick = onClick, modifier = modifier.shadow(elevation = intensity.dp, shape = MaterialTheme.shapes.small)
            .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
            shape = RoundedCornerShape(8.dp)) {
            Text(text = text, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            )
        }

        //Third Button layer
        Button(onClick = onClick, modifier = modifier.shadow(elevation = intensity.dp, shape = MaterialTheme.shapes.small)
            .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,

        ),
            shape = RoundedCornerShape(8.dp)
            ) {
            Text(text = text, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            )
        }

        // Connect with google composable

    }
    // Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }




}

@Preview
@Composable
fun PreviewNeonButton() {
    SoundLinkTheme {
        NeonButton(text = "Login", onClick = { })
    }
}

@Composable
fun GoogleButton() {
    // Proffesional Monocoloritic Button
    

}



