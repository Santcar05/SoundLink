package com.example.soundlink.features.growth.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
private val Surface2 = Color(0xFF101B26)
private val Outline1 = Color(0xFF1F2E3A)
private val TextPrimary = Color(0xFFE8F4FF)
private val TextMuted = Color(0xFF4A6070)
private val AccentPink = Color(0xFFFF2EFF)

data class SkillNodeData(
    val code: String,
    val label: String,
    val xFrac: Float,
    val yFrac: Float,
    val isUnlocked: Boolean
)

data class AchievementData(
    val title: String,
    val subtitle: String,
    val isUnlocked: Boolean
)

private val skillNodes = listOf(
    SkillNodeData("01", "Fundamentos", 0.50f, 0.11f, true),
    SkillNodeData("02", "Melodía", 0.25f, 0.44f, true),
    SkillNodeData("03", "Ritmo", 0.75f, 0.44f, true),
    SkillNodeData("04", "Producción", 0.12f, 0.78f, false),
    SkillNodeData("05", "Composición", 0.50f, 0.78f, false),
    SkillNodeData("06", "Mastering", 0.84f, 0.78f, false)
)

private val skillEdges = listOf(0 to 1, 0 to 2, 1 to 3, 1 to 4, 2 to 5)

private val achievements = listOf(
    AchievementData("Primera nota", "Publicaste tu primer post", true),
    AchievementData("Colaborador", "Primera colaboración exitosa", true),
    AchievementData("En racha", "7 días activo consecutivos", true),
    AchievementData("100 escuchas", "Tu música llegó a 100 oídos", false),
    AchievementData("Top Creator", "Entra al top 10 de la semana", false),
    AchievementData("Mentor", "Ayuda a 5 artistas emergentes", false)
)

@Composable
fun GrowthScreen(
    onActivitiesClick: () -> Unit = {},
    onDnaClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    navBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BgDeep,
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) { navBar() }
        }
    ) { contentPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
            .padding(contentPadding)
            .statusBarsPadding()
            .verticalScroll(scrollState)
    ) {
        // ── Header ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Tu Evolución",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = TextPrimary
            )
            Text(
                text = "Cada escucha, cada colaboración — cuenta.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        // ── Level card ───────────────────────────────────────────
        LevelCard()

        Spacer(Modifier.height(28.dp))

        // ── ADN Sonoro shortcut ──────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(PurpleNeon.copy(alpha = 0.18f), BlueNeon.copy(alpha = 0.08f))
                    )
                )
                .border(1.dp, PurpleNeon.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                .clickable { onDnaClick() }
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PurpleNeon.copy(alpha = 0.15f))
                        .border(1.dp, PurpleNeon.copy(alpha = 0.45f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "ADN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = PurpleNeon
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Tu ADN Sonoro",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        "Descubre y completa tu perfil musical",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                Text("›", fontSize = 22.sp, color = PurpleNeon, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Skill tree ───────────────────────────────────────────
        SectionLabel("Árbol de Habilidades")
        Spacer(Modifier.height(12.dp))
        SkillTree(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(Modifier.height(28.dp))

        // ── Achievements ─────────────────────────────────────────
        SectionLabel("Logros")
        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            achievements.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { ach ->
                        AchievementBadge(ach, modifier = Modifier.weight(1f))
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Activities shortcut ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface1)
                .border(1.dp, Outline1, RoundedCornerShape(16.dp))
                .clickable { onActivitiesClick() }
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BlueNeon.copy(alpha = 0.12f))
                        .border(1.dp, BlueNeon.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "ACT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = BlueNeon
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Actividades",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        "Completa retos para desbloquear habilidades",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                Text("›", fontSize = 22.sp, color = BlueNeon, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
    } // closes Scaffold content lambda
}

@Composable
private fun LevelCard() {
    var targetXp by remember { mutableFloatStateOf(0f) }
    val animatedXp by animateFloatAsState(
        targetValue = targetXp,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "xp"
    )
    LaunchedEffect(Unit) { targetXp = 0.73f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Surface1)
            .border(1.dp, Outline1, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Nivel 7",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = BlueNeon
                    )
                    Text(
                        text = "Productor Emergente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(BlueNeon.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                        .border(2.dp, BlueNeon, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "LV",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = BlueNeon
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("XP", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text(
                    "2 190 / 3 000",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
            }

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Outline1)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedXp)
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(listOf(BlueNeon, PurpleNeon))
                        )
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "${(animatedXp * 100).toInt()}% hacia Nivel 8",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun SkillTree(modifier: Modifier = Modifier) {
    val nodeRadius = 26.dp

    BoxWithConstraints(
        modifier = modifier.height(340.dp)
    ) {
        val w = maxWidth
        val h = maxHeight

        // Connecting lines drawn on Canvas behind nodes
        Canvas(modifier = Modifier.fillMaxSize()) {
            skillEdges.forEach { (fromIdx, toIdx) ->
                val fromNode = skillNodes[fromIdx]
                val toNode = skillNodes[toIdx]
                val isUnlockedEdge = skillNodes[toIdx].isUnlocked
                drawLine(
                    color = if (isUnlockedEdge) BlueNeon.copy(alpha = 0.55f) else Outline1,
                    start = Offset(fromNode.xFrac * size.width, fromNode.yFrac * size.height),
                    end = Offset(toNode.xFrac * size.width, toNode.yFrac * size.height),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Node composables overlaid
        skillNodes.forEach { node ->
            val cx = w * node.xFrac
            val cy = h * node.yFrac

            Column(
                modifier = Modifier
                    .offset(x = cx - nodeRadius, y = cy - nodeRadius)
                    .width(nodeRadius * 2),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(nodeRadius * 2)
                        .then(
                            if (node.isUnlocked)
                                Modifier.shadow(
                                    elevation = 10.dp,
                                    shape = CircleShape,
                                    ambientColor = BlueNeon,
                                    spotColor = BlueNeon
                                )
                            else Modifier
                        )
                        .clip(CircleShape)
                        .background(
                            if (node.isUnlocked)
                                Brush.radialGradient(
                                    listOf(BlueNeon.copy(0.22f), Surface1)
                                )
                            else Brush.radialGradient(listOf(Surface2, Surface2))
                        )
                        .border(
                            width = if (node.isUnlocked) 2.dp else 1.5.dp,
                            color = if (node.isUnlocked) BlueNeon else Outline1,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = node.code,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = if (node.isUnlocked) BlueNeon else TextMuted
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = node.label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (node.isUnlocked) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    color = if (node.isUnlocked) TextPrimary else TextMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun AchievementBadge(ach: AchievementData, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (ach.isUnlocked) BlueNeon.copy(alpha = 0.07f) else Surface1
            )
            .border(
                width = 1.dp,
                color = if (ach.isUnlocked) BlueNeon.copy(alpha = 0.4f) else Outline1,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(10.dp)) {
                    drawCircle(color = if (ach.isUnlocked) BlueNeon else Outline1)
                }
                Spacer(Modifier.width(8.dp))
                if (ach.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(BlueNeon.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "OK",
                            fontSize = 9.sp,
                            color = BlueNeon,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = ach.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (ach.isUnlocked) TextPrimary else TextMuted,
                maxLines = 1
            )
            Text(
                text = ach.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            ),
            color = BlueNeon
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.dp)
                .background(Brush.horizontalGradient(listOf(BlueNeon, PurpleNeon)))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050A0F)
@Composable
fun GrowthScreenPreview() {
    SoundLinkTheme {
        GrowthScreen()
    }
}
