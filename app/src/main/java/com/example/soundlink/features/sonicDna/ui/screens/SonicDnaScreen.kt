package com.example.soundlink.features.sonicDna.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundlink.app.theme.BlueNeon
import com.example.soundlink.app.theme.PurpleNeon
import com.example.soundlink.app.theme.SoundLinkTheme

private val BgDeep = Color(0xFF050A0F)
private val Surface1 = Color(0xFF0A121A)
private val Outline1 = Color(0xFF1F2E3A)
private val TextPrimary = Color(0xFFE8F4FF)
private val TextMuted = Color(0xFF4A6070)

data class DnaOption(val label: String, val sublabel: String)
data class DnaStep(val question: String, val subtitle: String, val options: List<DnaOption>)
data class SonicProfile(val vibe: String, val role: String, val genre: String, val style: String)

val sonicDnaSteps = listOf(
    DnaStep(
        question = "¿Cuál es tu vibe sonora?",
        subtitle = "La energía que defines en tu música",
        options = listOf(
            DnaOption("Etéreo", "Texturas y atmósferas"),
            DnaOption("Intenso", "Energía sin límites"),
            DnaOption("Melódico", "Armonías que fluyen"),
            DnaOption("Rítmico", "El groove lo es todo")
        )
    ),
    DnaStep(
        question = "¿Cuál es tu rol?",
        subtitle = "Cómo contribuyes al proceso creativo",
        options = listOf(
            DnaOption("Productor", "Creas el lienzo"),
            DnaOption("Vocalista", "Tu voz es el instrumento"),
            DnaOption("Instrumentista", "Las cuerdas hablan"),
            DnaOption("Compositor", "Las letras son tuyas")
        )
    ),
    DnaStep(
        question = "¿Tus influencias?",
        subtitle = "Los universos sonoros que te mueven",
        options = listOf(
            DnaOption("Electrónico", "Síntesis y beats"),
            DnaOption("Jazz / Soul", "Improvisación pura"),
            DnaOption("Rock / Metal", "Energía en vivo"),
            DnaOption("Urbano", "Cultura callejera")
        )
    ),
    DnaStep(
        question = "¿Cómo colaboras?",
        subtitle = "Tu estilo de trabajo con otros artistas",
        options = listOf(
            DnaOption("Líder", "Diriges la visión"),
            DnaOption("Co-creador", "Construyes juntos"),
            DnaOption("Aprendiz", "Absorbes y creces"),
            DnaOption("Mentor", "Guías el camino")
        )
    )
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SonicDnaScreen(
    onComplete: (SonicProfile) -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(0) }
    val selections = remember { mutableStateListOf(-1, -1, -1, -1) }

    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val barAnimations = List(8) { i ->
        infiniteTransition.animateFloat(
            initialValue = 0.18f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(480 + i * 130, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar$i"
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ── Top bar ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Surface1)
                        .clickable { if (currentStep > 0) currentStep-- else onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = BlueNeon,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(sonicDnaSteps.size) { i ->
                        val isActive = i == currentStep
                        val isPast = i < currentStep
                        val dotW by animateDpAsState(
                            targetValue = if (isActive) 28.dp else 8.dp,
                            animationSpec = tween(300, easing = FastOutSlowInEasing),
                            label = "dot$i"
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(dotW)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isActive -> BlueNeon
                                        isPast -> BlueNeon.copy(alpha = 0.4f)
                                        else -> Outline1
                                    }
                                )
                        )
                    }
                }

                Text(
                    text = "${currentStep + 1}/${sonicDnaSteps.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted
                )
            }

            // ── Frequency visualizer ──────────────────────────────
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 24.dp)
            ) {
                val n = barAnimations.size
                val spacing = size.width / n
                val barW = spacing * 0.42f
                barAnimations.forEachIndexed { idx, anim ->
                    val barH = size.height * anim.value
                    val cx = idx * spacing + spacing / 2f
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF00C8FF), Color(0xFF7B61FF)),
                            startY = (size.height - barH) / 2f,
                            endY = (size.height + barH) / 2f
                        ),
                        topLeft = Offset(cx - barW / 2f, (size.height - barH) / 2f),
                        size = Size(barW, barH),
                        cornerRadius = CornerRadius(barW / 2f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Step content ──────────────────────────────────────
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    val forward = targetState > initialState
                    slideInHorizontally(
                        initialOffsetX = { if (forward) it else -it },
                        animationSpec = tween(320, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(280)) togetherWith slideOutHorizontally(
                        targetOffsetX = { if (forward) -it else it },
                        animationSpec = tween(280)
                    ) + fadeOut(tween(200))
                },
                label = "step",
                modifier = Modifier.weight(1f)
            ) { step ->
                val data = sonicDnaSteps[step]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = data.question,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = data.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(28.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        data.options.chunked(2).forEachIndexed { rowIdx, row ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                row.forEachIndexed { colIdx, opt ->
                                    val optIdx = rowIdx * 2 + colIdx
                                    DnaOptionCard(
                                        option = opt,
                                        isSelected = selections[step] == optIdx,
                                        onClick = { selections[step] = optIdx },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── CTA button ────────────────────────────────────────
            val canGo = selections[currentStep] >= 0
            val isLast = currentStep == sonicDnaSteps.size - 1

            AnimatedVisibility(
                visible = canGo,
                enter = fadeIn(tween(220)) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ),
                exit = fadeOut(tween(150))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            if (isLast) {
                                onComplete(
                                    SonicProfile(
                                        vibe = sonicDnaSteps[0].options[selections[0].coerceAtLeast(0)].label,
                                        role = sonicDnaSteps[1].options[selections[1].coerceAtLeast(0)].label,
                                        genre = sonicDnaSteps[2].options[selections[2].coerceAtLeast(0)].label,
                                        style = sonicDnaSteps[3].options[selections[3].coerceAtLeast(0)].label
                                    )
                                )
                            } else {
                                currentStep++
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = BlueNeon,
                                spotColor = BlueNeon
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueNeon,
                            contentColor = BgDeep
                        )
                    ) {
                        Text(
                            text = if (isLast) "Descubrir mi ADN Sonoro" else "Siguiente",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            }

            if (!canGo) Spacer(Modifier.height(86.dp))
        }
    }
}

@Composable
private fun DnaOptionCard(
    option: DnaOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.96f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "scale"
    )
    val accentAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "accent"
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) BlueNeon.copy(alpha = 0.10f) else Surface1)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                brush = if (isSelected)
                    Brush.linearGradient(listOf(BlueNeon, PurpleNeon))
                else
                    Brush.linearGradient(listOf(Outline1, Outline1)),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                BlueNeon.copy(alpha = accentAlpha),
                                PurpleNeon.copy(alpha = accentAlpha)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isSelected) BlueNeon else TextPrimary
                )
                Text(
                    text = option.sublabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) BlueNeon.copy(alpha = 0.7f) else TextMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050A0F)
@Composable
fun SonicDnaScreenPreview() {
    SoundLinkTheme {
        SonicDnaScreen()
    }
}
