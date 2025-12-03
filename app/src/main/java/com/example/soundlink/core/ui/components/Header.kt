package com.example.soundlink.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme

@Composable
fun Header() {
    val borderColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)

            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
 {
        Spacer(modifier = Modifier.padding(4.dp))
        NeonTextMultiLayer(text = "SoundLink", fontSize = 32)

        Spacer(modifier = Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .height(56.dp)){
            // Two icons
            Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Logo", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.padding(14.dp))
            Icon(painter = painterResource(id = R.drawable.bell), contentDescription = "Logo", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.padding(4.dp))
        }

    }
}

@Preview
@Composable
fun HeaderPreview() {
    SoundLinkTheme {
        Header()
    }
}
