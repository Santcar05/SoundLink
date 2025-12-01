package com.example.soundlink.features.auth.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

            NeonTextMultiLayer(text = "Register", fontSize = 48)

            Text(
                "Únete y conecta con artistas y creadores",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(28.dp))

            NeonTextField(
                field = "Full Name",
                iconId = R.drawable.logo,
                textValue = name,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { name = it }
            )

            Spacer(Modifier.height(12.dp))

            NeonTextField(
                field = "Email",
                iconId = R.drawable.logo,
                textValue = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { email = it }
            )

            Spacer(Modifier.height(12.dp))

            PasswordNeonTextField(
                field = "Password",
                iconId = R.drawable.logo,
                textValue = pass,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { pass = it }
            )

            Spacer(Modifier.height(12.dp))

            NeonTextField(
                field = "Age",
                iconId = R.drawable.logo,
                textValue = age,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { age = it }
            )

            Spacer(Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    "✔ Colabora con otros artistas",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    "✔ Guarda y comparte tu legado musical",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    "✔ Acceso a herramientas exclusivas",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            NeonButton(
                text = "Create Account",
                onClick = {
                    val ageLong = age.toLongOrNull()

                    if (ageLong == null) {
                        Toast.makeText(context, "Age must be a number", Toast.LENGTH_SHORT).show()
                        return@NeonButton
                    }

                    registerViewModel.register(name, email, pass, ageLong) { success ->
                        if (success) {
                            Toast.makeText(context, "Register successful", Toast.LENGTH_SHORT).show()
                            onRegisterClick(name, email, pass, ageLong)
                        } else {
                            Toast.makeText(context, "Register failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.clickable { onLoginClick() }
            )
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