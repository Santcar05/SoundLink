package com.example.soundlink.core.ui.components
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme


@Composable
fun AuthButton(
    text: String = "Continuar con Google",
    icon: Painter? = null,
    modifier: Modifier = Modifier,
    elevationDefault: Dp = 6.dp,
    showRipple: Boolean = true, // Controls if the ripple effect is shown (ripple)
    onClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    // Interaction source to detect when the button is pressed
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    // Animation when the button is pressed
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 120)
    )

    val elevation by animateDpAsState(
        targetValue = if (pressed) (elevationDefault / 2f) else elevationDefault,
        animationSpec = tween(durationMillis = 180)
    )


    val backgroundColor: Color = if (isDark) colorScheme.surface else colorScheme.primaryContainer
    val contentColor: Color = if (isDark) Color.White else Color.Black

    // Modifier to handle the ripple effect
    val clickableModifier = if (showRipple) {
        modifier
    } else {
        // usamos clickable con indication = null overload a través de .clickable(...) si necesitas,
        // pero para mantener compatibilidad en muchas versiones simplemente usaremos clickable con indication null.
        modifier
    }

    Surface(
        modifier = clickableModifier
            .scale(scale)
            .then(
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = if (showRipple) null else null, // si quieres pasar null explícito, lo puedes hacer
                    onClick = onClick
                )
            ),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = colorScheme.primary, // icono en azul
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun PreviewAuthButton() {
    SoundLinkTheme {
        AuthButton( onClick = { }, icon = painterResource(id = R.drawable.logo))
    }
}
