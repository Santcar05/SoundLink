package com.example.soundlink.feature.createpost

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.features.feed.ui.screens.createpost.CreatePostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    createPostViewModel: CreatePostViewModel = viewModel(),
    onPost: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val state by createPostViewModel.state.collectAsState()

    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        uris.forEach { createPostViewModel.onAddMedia(it) }
    }

    val context = LocalContext.current
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("Crear publicación", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = state.title,
                onValueChange = createPostViewModel::onTitleChange,
                label = { Text("Título") },
                isError = state.titleError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            if (state.titleError != null) {
                Text(state.titleError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = createPostViewModel::onDescriptionChange,
                label = { Text("Descripción") },
                isError = state.descriptionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                shape = RoundedCornerShape(8.dp)
            )
            if (state.descriptionError != null) {
                Text(state.descriptionError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(12.dp))

            // Chips de tags, simple
            var tagInput by remember { mutableStateOf("") }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("Agregar tag") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        val trimmed = tagInput.trim()
                        if (trimmed.isNotEmpty() && !state.tags.contains(trimmed))
                            createPostViewModel.onTagsChange(state.tags + trimmed)
                        tagInput = ""
                    },
                    enabled = tagInput.isNotBlank()
                ) {
                    Text("Agregar")
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                state.tags.forEach { tag ->
                    AssistChip(
                        label = { Text(tag) },
                        onClick = {},
                        trailingIcon = {
                            IconButton(onClick = { createPostViewModel.onTagsChange(state.tags - tag) }) {
                                Icon(Icons.Default.Close, "Quitar tag", modifier = Modifier.size(16.dp))
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Medios", style = MaterialTheme.typography.titleMedium)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.media.size) { idx ->
                    val uri = state.media[idx]
                    Box(modifier = Modifier.size(100.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = uri),
                            contentDescription = "Imagen o video seleccionado",
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { createPostViewModel.onRemoveMedia(uri) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Eliminar medio", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                // Botón para añadir nuevas imágenes/videos
                item {
                    IconButton(
                        modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp)),
                        onClick = { imageLauncher.launch("image/*") } // Cambia a "video/*" si lo prefieres
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir imágenes o videos", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    createPostViewModel.validateAndPost(
                        onSuccess = {
                            Toast.makeText(context, "Post creado exitosamente", Toast.LENGTH_SHORT).show()
                            onPost
                        },
                        onError = { /* Toast o Snackbar */ }
                    )
                },
                enabled = !state.isPosting,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (state.isPosting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Publicando...")
                } else {
                    Text("Publicar")
                }
            }
        }
    }
}

@Preview
@Composable
fun CreatePostScreenPreview() {
    SoundLinkTheme {
        CreatePostScreen(
            createPostViewModel = viewModel(),
            onPost = {},
            onCancel = {}
        )
    }
}