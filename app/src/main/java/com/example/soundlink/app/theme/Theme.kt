package com.example.soundlink.app.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BlueNeon,
    onPrimary = Color.Black,
    primaryContainer = BlueDark,

    secondary = PurpleNeon,
    onSecondary = Color.Black,

    tertiary = PinkNeon,
    onTertiary = Color.Black,
    tertiaryContainer = PinkNeon,

    background = BackgroundDark,
    onBackground = Color.White,

    surface = SurfaceDark,
    onSurface = Color.White,


    error = ErrorNeon,
    outline = OutlineDark,

   onSurfaceVariant = Gray


)

private val LightColorScheme = lightColorScheme(

    primary = BluePrimary,
    onPrimary = BlueOnPrimary,
    primaryContainer = BluePrimaryContainer,
    onPrimaryContainer = Color.Black,

    secondary = BlueSecondary,
    onSecondary = Color.White,
    secondaryContainer = BlueSecondaryContainer,
    onSecondaryContainer = Color.Black,

    tertiary = PurpleLight,
    onTertiary = Color.White,
    tertiaryContainer = PinkLight,
    onTertiaryContainer = Color.White,

    background = BackgroundLight,
    onBackground = Color.Black,

    surface = SurfaceLight,
    onSurface = Color.Black,
    surfaceVariant = SurfaceElevatedLight,
    onSurfaceVariant = GrayLight,

    outline = OutlineLight,
    error = ErrorLight
)

@Composable
fun SoundLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
