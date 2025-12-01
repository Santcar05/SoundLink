package com.example.soundlink.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.ui.navigation.AppNavigation
import com.example.soundlink.features.auth.ui.screens.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoundLinkTheme {
                AppNavigation()
            }
        }
    }
}
