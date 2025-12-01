package com.example.soundlink.app.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.soundlink.R

// Fuentes en /res/font/
val PlusJakarta = FontFamily(
    Font(R.font.plusjakartasans_light, FontWeight.Light),
    Font(R.font.plusjakartasans_regular, FontWeight.Normal),
    Font(R.font.plusjakartasans_medium, FontWeight.Medium),
    Font(R.font.plusjakartasans_semibold, FontWeight.SemiBold),
    Font(R.font.plusjakartasans_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakarta,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)