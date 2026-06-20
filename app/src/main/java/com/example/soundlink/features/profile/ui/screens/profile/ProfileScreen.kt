package com.example.soundlink.features.profile.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.soundlink.app.theme.BlueNeon
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
    modifier: Modifier = Modifier,
    onDnaClick: () -> Unit = {}
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

            Spacer(modifier = Modifier.height(16.dp))

            // ADN Sonoro banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.07f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.30f),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable(onClick = onDnaClick)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "ADN",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "ADN Sonoro",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Descubre tu perfil musical único",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        "›",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
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
private fun NeonFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val glowIntensity by animateFloatAsState(
        targetValue = if (isFocused) 14f else 0f,
        animationSpec = tween(280),
        label = "fieldGlow"
    )
    val errorColor = Color(0xFFFF1744)
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.shadow(
                elevation = glowIntensity.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false,
                ambientColor = if (isError) errorColor else BlueNeon,
                spotColor = if (isError) errorColor else BlueNeon
            )
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                label = { Text(label) },
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                trailingIcon = trailingIcon,
                isError = isError,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedTextFieldColors(
                    focusedContainerColor = Color(0xFF0A121A),
                    unfocusedContainerColor = Color(0xFF0A121A),
                    focusedBorderColor = BlueNeon,
                    unfocusedBorderColor = Color(0xFF1F2E3A),
                    cursorColor = BlueNeon,
                    focusedTextColor = Color(0xFFE8F4FF),
                    unfocusedTextColor = Color(0xFFE8F4FF),
                    focusedLabelColor = BlueNeon,
                    unfocusedLabelColor = Color(0xFF4A6070),
                    errorBorderColor = errorColor,
                    errorTextColor = errorColor,
                    errorLabelColor = errorColor
                )
            )
        }
        if (errorMessage != null) {
            Text(
                errorMessage,
                color = errorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
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
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        NeonFormField(
            value = state.user.name,
            onValueChange = { onUpdate("name", it) },
            label = "Nombre",
            isError = state.formErrors.name != null,
            errorMessage = state.formErrors.name
        )

        NeonFormField(
            value = state.user.email,
            onValueChange = { onUpdate("email", it) },
            label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.formErrors.email != null,
            errorMessage = state.formErrors.email
        )

        NeonFormField(
            value = state.user.password,
            onValueChange = { onUpdate("password", it) },
            label = "Contraseña",
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        if (passwordVisible) "Ocultar" else "Mostrar",
                        color = BlueNeon,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            isError = state.formErrors.password != null,
            errorMessage = state.formErrors.password
        )

        NeonFormField(
            value = if (state.user.age == 0) "" else state.user.age.toString(),
            onValueChange = { onUpdate("age", it.filter { ch -> ch.isDigit() }) },
            label = "Edad",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.formErrors.age != null,
            errorMessage = state.formErrors.age
        )

        NeonFormField(
            value = state.user.avatarUrl,
            onValueChange = { onUpdate("avatar", it) },
            label = "URL de avatar (opcional)"
        )

        // Verified toggle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0A121A))
                .border(1.dp, Color(0xFF1F2E3A), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Cuenta verificada",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFFE8F4FF)
                    )
                    Text(
                        "Solicita verificación al equipo",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A6070)
                    )
                }
                Switch(
                    checked = state.user.verified,
                    onCheckedChange = { onUpdate("verified", it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF050A0F),
                        checkedTrackColor = BlueNeon,
                        uncheckedThumbColor = Color(0xFF4A6070),
                        uncheckedTrackColor = Color(0xFF1F2E3A),
                        uncheckedBorderColor = Color(0xFF1F2E3A)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Save / Discard buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = BlueNeon,
                        spotColor = BlueNeon
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueNeon,
                    contentColor = Color(0xFF050A0F)
                )
            ) {
                Text(
                    "Guardar",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            OutlinedButton(
                onClick = onDiscard,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Descartar", color = Color(0xFF4A6070))
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