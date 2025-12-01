package com.example.soundlink.features.auth.ui.screens.login


import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundlink.R
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.domain.usecases.GetUserUseCase
import com.example.soundlink.core.ui.components.AuthButton
import com.example.soundlink.core.ui.components.NeonButton
import com.example.soundlink.core.ui.components.NeonTextField
import com.example.soundlink.core.ui.components.NeonTextMultiLayer
import com.example.soundlink.core.ui.components.PasswordNeonTextField
import com.example.soundlink.core.ui.session.SessionViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    sessionViewModel: SessionViewModel,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    var state by remember { mutableStateOf(LoginState()) }

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(16.dp)
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.25f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

            NeonTextMultiLayer("SOUNDLINK", 45)

            Spacer(modifier = Modifier.weight(0.025f))

            Text(
                text = "Conecta con artistas de todo el mundo",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.weight(0.05f))

            NeonTextField(
                field = "Email",
                iconId = R.drawable.logo,
                textValue = state.email,
                onValueChange = { state = state.copy(email = it) }
            )

            Spacer(modifier = Modifier.weight(0.025f))

            PasswordNeonTextField(
                field = "Password",
                iconId = R.drawable.logo,
                textValue = state.password,
                onValueChange = { state = state.copy(password = it) }
            )

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { /* TODO */ }
                    .align(Alignment.End)
            )

            NeonButton(
                text = "Login",
                onClick = {
                    if (state.email.isEmpty() || state.password.isEmpty()) {
                        Toast.makeText(context, "Email and password are required", Toast.LENGTH_SHORT).show()
                        return@NeonButton
                    }

                    loginViewModel.login(state.email, state.password, sessionViewModel) { success ->
                        if (success) Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        else Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                },
                intensity = 40f,
                modifier = Modifier.height(48.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            AuthButton(
                onClick = {},
                icon = painterResource(id = R.drawable.google),
                modifier = Modifier.height(60.dp).fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(0.1f))

            AuthButton(
                text = "Continuar con Spotify",
                onClick = {},
                icon = painterResource(id = R.drawable.spotify),
                modifier = Modifier.height(60.dp).fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(0.3f))

            Text(
                text = "Don't have an account? Register",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onRegisterClick() }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    SoundLinkTheme {
        LoginScreen(modifier = Modifier,
            loginViewModel = LoginViewModel( loginUseCase = AppContainer.LoginUseCase, getCurrentUser = AppContainer.GetUserUseCase),
            sessionViewModel = SessionViewModel(GetUserUseCase(AppContainer.userRepository)),
            onRegisterClick = {},
            onLoginClick = {},
            )
    }
}
