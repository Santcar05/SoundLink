package com.example.soundlink.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun NeonTextMultiLayer( text: String, fontSize: Int) {
    Box(contentAlignment = Alignment.Center) {
        // Capa 1: Sombra exterior difusa
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Transparent,
                shadow = Shadow(
                    color = Color(0xFF00FFFF),
                    offset = Offset(0f, 0f),
                    blurRadius = 5f
                )
            )
        )

        // Capa 2: Sombra interior
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Transparent,
                shadow = Shadow(
                    color = Color(0xFF00AAFF),
                    offset = Offset(0f, 0f),
                    blurRadius = 10f
                )
            )
        )

        // Capa 3: Texto principal
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}
