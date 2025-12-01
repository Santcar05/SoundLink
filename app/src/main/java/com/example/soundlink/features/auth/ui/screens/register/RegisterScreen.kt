package com.example.soundlink.features.auth.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.soundlink.R
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.core.ui.components.NeonButton
import com.example.soundlink.core.ui.components.NeonTextField
import com.example.soundlink.core.ui.components.NeonTextMultiLayer
import com.example.soundlink.core.ui.components.PasswordNeonTextField
import com.example.soundlink.core.ui.session.SessionViewModel
import com.example.soundlink.features.auth.domain.usecases.RegisterUseCase

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: (name: String, email: String, pass: String, age: Long) -> Unit,
    sessionViewModel: SessionViewModel,
    registerViewModel: RegisterViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Scaffold { innerPadding ->

        Column(
            modifier = modifier
                .padding(16.dp)
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.25f))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

            NeonTextMultiLayer(text = "CREATE ACCOUNT", fontSize = 40)

            Spacer(modifier = Modifier.weight(0.035f))

            Text(
                text = "Únete y conecta con artistas y creadores",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.weight(0.05f))

            // Inputs
            NeonTextField(
                field = "Full Name",
                iconId = R.drawable.logo,
                textValue = name,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { name = it }
            )
            Spacer(modifier = Modifier.height(12.dp))

            NeonTextField(
                field = "Email",
                iconId = R.drawable.logo,
                textValue = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { email = it }
            )
            Spacer(modifier = Modifier.height(12.dp))

            PasswordNeonTextField(
                field = "Password",
                iconId = R.drawable.logo,
                textValue = pass,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { pass = it }
            )
            Spacer(modifier = Modifier.height(12.dp))

            NeonTextField(
                field = "Age",
                iconId = R.drawable.logo,
                textValue = age,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { age = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Register button
            NeonButton(
                text = "Create Account",
                onClick = {
                    if (age.toIntOrNull() != null) {
                        onRegisterClick(name, email, pass, age.toLong())
                    }
                },
                intensity = 40f,
                modifier = Modifier.height(48.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Already have an account?
            Text(
                text = "Already have an account? Login",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onLoginClick() }
            )

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    SoundLinkTheme {
        RegisterScreen(
            onLoginClick = {},
            onRegisterClick = { name, email, pass, age ->

            },
            sessionViewModel = SessionViewModel(getUserUseCase = AppContainer.GetUserUseCase),
            registerViewModel = RegisterViewModel(registerUseCase = AppContainer.RegisterUseCase, sessionViewModel = SessionViewModel(getUserUseCase = AppContainer.GetUserUseCase))
        )
    }
}