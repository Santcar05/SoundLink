package com.example.soundlink.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonTextField(field : String,iconId : Int,modifier: Modifier = Modifier, textValue: String = "" ) {
    var text by remember { mutableStateOf(textValue) }
    var isFocused by remember { mutableStateOf(false) }

    val neonColor = Color(0xFF00FFFF)

    val glowIntensity by animateFloatAsState(
        targetValue = if (isFocused) 20f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "glowAnimation"
    )

    Box(
        modifier = modifier

            .shadow(
                elevation = glowIntensity.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false,
                ambientColor = neonColor,
                spotColor = neonColor
            )
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Row {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = field,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        field,
                        color = if (isFocused) neonColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            colors = outlinedTextFieldColors(
                // FONDO COMPLETAMENTE TRANSPARENTE
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,

                errorContainerColor = Color.Transparent,


                // BORDES
                focusedBorderColor = neonColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = Color.Red,

                // TEXTO
                focusedTextColor = neonColor,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor =neonColor,
                errorTextColor = Color.Red,

                // CURSOR
                cursorColor = neonColor,
                errorCursorColor = Color.Red,

                // LABEL
                focusedLabelColor = neonColor,
                unfocusedLabelColor = Color.Gray,
                disabledLabelColor = Color.Gray,
                errorLabelColor = Color.Red,

                // PLACEHOLDER
                focusedPlaceholderColor = Color(0xFF888888).copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color(0xFF888888).copy(alpha = 0.7f),
                disabledPlaceholderColor = Color.Gray,
                errorPlaceholderColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview
@Composable
fun PreviewNeonTextField() {
    SoundLinkTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFF1A1A1A))

        ) {
            NeonTextField("Email", iconId = R.drawable.logo)
        }
    }
}


// PASSWORD TEXTFIELD with eye icon to show/hide password
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordNeonTextField(
    field: String,
    iconId: Int,
    modifier: Modifier = Modifier,
    textValue: String = ""
) {
    var text by remember { mutableStateOf(textValue) }
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val neonColor = Color(0xFF00FFFF)

    val glowIntensity by animateFloatAsState(
        targetValue = if (isFocused) 20f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "glowPassword"
    )

    Box(
        modifier = modifier.shadow(
            elevation = glowIntensity.dp,
            shape = RoundedCornerShape(12.dp),
            clip = false,
            ambientColor = neonColor,
            spotColor = neonColor
        )
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Row {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = field,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        field,
                        color = if (isFocused) neonColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },

            // ----------- PASSWORD VISUAL TRANSFORMATION -----------
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },

            // ----------- ICON TO SHOW  / HIDE -------------
            trailingIcon = {
                val eyeIcon = if (passwordVisible)
                    R.drawable.logo   // ICON
                else
                    R.drawable.logo       // ICON

                Icon(
                    painter = painterResource(id = eyeIcon),
                    contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                    modifier = Modifier
                        .size(26.dp)
                        .padding(end = 4.dp)
                        .clickable { passwordVisible = !passwordVisible },
                    tint = if (isFocused) neonColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },

            colors = outlinedTextFieldColors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,

                // Bordes
                focusedBorderColor = neonColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = Color.Red,

                // Texto
                focusedTextColor = neonColor,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTextColor = neonColor,
                errorTextColor = Color.Red,

                // Cursor
                cursorColor = neonColor,
                errorCursorColor = Color.Red,

                // Label
                focusedLabelColor = neonColor,
                unfocusedLabelColor = Color.Gray,
                disabledLabelColor = Color.Gray,
                errorLabelColor = Color.Red,

                // Placeholder
                focusedPlaceholderColor = Color(0xFF888888).copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color(0xFF888888).copy(alpha = 0.7f),
                disabledPlaceholderColor = Color.Gray,
                errorPlaceholderColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
@Preview
@Composable
fun PreviewPasswordNeonTextField() {
    SoundLinkTheme {
        PasswordNeonTextField("Password", iconId = R.drawable.logo)
    }
}
