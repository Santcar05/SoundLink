package com.example.soundlink.features.profile.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundlink.R
import com.example.soundlink.app.di.AppContainer
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.domain.model.User
import com.example.soundlink.core.ui.session.SessionViewModel

/**
 * Versión corregida: la pantalla es ahora scrollable verticalmente usando verticalScroll.
 * - Evita usar Modifier.scrollable para layouts largos; verticalScroll + rememberScrollState es la forma correcta.
 * - Añadimos imePadding() y navigationBarsPadding() para evitar que el teclado o barra de navegación oculten los botones.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Mostrar mensaje de éxito o error mediante Snackbar
    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
        }
        state.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        // Hacemos todo scrollable verticalmente. Usamos imePadding() para que el teclado no oculte campos,
        // y navigationBarsPadding() para respetar barra de navegación en dispositivos con gestos.
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar grande con acción para cambiar (placeholder)
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val avatarRes = if (state.user.avatarUrl.isBlank()) R.drawable.user else R.drawable.user
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(122.dp)
                        .clip(CircleShape)
                        .clickable { /* abrir selector de imagen si procede */ },
                    contentScale = ContentScale.Crop
                )
                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 6.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Cambiar avatar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Nombre + verified badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.user.name.ifBlank { "Usuario" },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(10.dp))
                AnimatedVisibility(visible = state.user.verified) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = "Verificado",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Switch entre ver y editar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (state.isEditing) "Modo edición" else "Modo vista",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )

                val scale by animateFloatAsState(
                    if (state.isEditing) 1.03f else 1f,
                    animationSpec = androidx.compose.animation.core.tween(350, easing = LinearOutSlowInEasing)
                )
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.toggleEdit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (state.isEditing) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primary),
                    modifier = Modifier.scale(scale)
                ) {
                    Text(if (state.isEditing) "Cancelar edición" else "Editar perfil", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido (campos) - si isEditing true, mostramos TextFields editables
            Crossfade(targetState = state.isEditing) { editing ->
                if (editing) {
                    EditProfileForm(state = state, onUpdate = { field, value ->
                        when (field) {
                            "name" -> viewModel.updateName(value as String)
                            "email" -> viewModel.updateEmail(value as String)
                            "password" -> viewModel.updatePassword(value as String)
                            "age" -> {
                                val intAge = (value as String).toIntOrNull() ?: 0
                                viewModel.updateAge(intAge)
                            }
                            "avatar" -> viewModel.updateAvatarUrl(value as String)
                            "verified" -> viewModel.updateVerified(value as Boolean)
                        }
                    }, onSave = { viewModel.saveProfile() }, onDiscard = { viewModel.toggleEdit() })
                } else {
                    // Vista solo lectura
                    ViewProfile(state = state)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Indicador de guardado / Loader
            AnimatedVisibility(visible = state.isSaving) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
            }

            // Un pequeño padding extra al final para asegurar que los botones queden por encima de la barra de navegación/teclado
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ViewProfile(state: ProfileUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InfoRow(label = "Email", value = state.user.email)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(label = "Edad", value = state.user.age.toString())
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(label = "ID", value = state.user.id.toString())
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileForm(
    state: ProfileUiState,
    onUpdate: (field: String, value: Any) -> Unit,
    onSave: () -> Unit,
    onDiscard: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Nombre
        OutlinedTextField(
            value = state.user.name,
            onValueChange = { onUpdate("name", it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre") },
            isError = state.formErrors.name != null,
            singleLine = true,
            colors = outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        state.formErrors.name?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

        Spacer(modifier = Modifier.height(10.dp))

        // Email
        OutlinedTextField(
            value = state.user.email,
            onValueChange = { onUpdate("email", it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.formErrors.email != null,
            singleLine = true
        )
        state.formErrors.email?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

        Spacer(modifier = Modifier.height(10.dp))

        // Password
        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = state.user.password,
            onValueChange = { onUpdate("password", it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Ocultar" else "Mostrar", color = MaterialTheme.colorScheme.primary)
                }
            },
            isError = state.formErrors.password != null
        )
        state.formErrors.password?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

        Spacer(modifier = Modifier.height(10.dp))

        // Age
        OutlinedTextField(
            value = if (state.user.age == 0) "" else state.user.age.toString(),
            onValueChange = {
                val digits = it.filter { ch -> ch.isDigit() }
                onUpdate("age", digits)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.formErrors.age != null,
            singleLine = true
        )
        state.formErrors.age?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

        Spacer(modifier = Modifier.height(12.dp))

        // Avatar URL (opcional)
        OutlinedTextField(
            value = state.user.avatarUrl,
            onValueChange = { onUpdate("avatar", it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("URL de avatar (opcional)") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Verified toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Cuenta verificada", modifier = Modifier.weight(1f))
            Switch(
                checked = state.user.verified,
                onCheckedChange = { onUpdate("verified", it) },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Acción: Guardar / Cancelar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
            OutlinedButton(
                onClick = onDiscard,
                modifier = Modifier.weight(1f),
            ) {
                Text("Descartar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SoundLinkTheme {
        val vm = ProfileViewModel(
            sessionViewModel = SessionViewModel(
                getUserUseCase = AppContainer.GetUserUseCase
            ),
            updateUserUseCase = AppContainer.UpdateUserUseCase
        )
        vm.toggleEdit()
        ProfileScreen(viewModel = vm)
    }
}