package com.example.soundlink.features.feed.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme


//Floating Action Circle Button
@Composable
fun FabButton(onClick: () -> Unit) {
    val primary  =MaterialTheme.colorScheme.primary
    // Circle Button
    Box(modifier = Modifier
        .clickable(onClick = onClick)
        .size(56.dp),
        contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(56.dp)) {
            drawCircle(
                color = primary,
                style = Fill
            )


        }
        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = "Add",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun FabButtonPreview() {
    SoundLinkTheme {
        FabButton(onClick = {})
    }
}
