package com.example.soundlink.features.discovery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

data class ArtistMatch(
    val name: String,
    val role: String,
    val genre: String,
    val compatibility: Int,
    val vibe: String,
    val accentColor: Color
)

private val genres = listOf("Todos", "Electrónico", "Rock", "Jazz", "Urbano", "Clásico")

private val sampleArtists = listOf(
    ArtistMatch("Luna Martínez", "Productora", "Electrónico", 94, "Etéreo", Color(0xFF00C8FF)),
    ArtistMatch("Marcos Vidal", "Vocalista", "Jazz / Soul", 88, "Melódico", Color(0xFF7B61FF)),
    ArtistMatch("Sofía Chen", "Instrumentista", "Rock", 82, "Intenso", Color(0xFFFF2EFF)),
    ArtistMatch("Diego Torres", "Compositor", "Urbano", 79, "Rítmico", Color(0xFF00FF9B)),
    ArtistMatch("Ana Ruiz", "Instrumentista", "Jazz / Soul", 75, "Melódico", Color(0xFFFFE600)),
    ArtistMatch("Carlos Nava", "Productor", "Electrónico", 71, "Etéreo", Color(0xFF00C8FF)),
)

@Composable
fun DiscoveryScreen(
    modifier: Modifier = Modifier,
    navBar: @Composable () -> Unit = {}
) {
    var selectedGenre by remember { mutableStateOf("Todos") }

    val filteredArtists = if (selectedGenre == "Todos")
        sampleArtists
    else
        sampleArtists.filter { it.genre == selectedGenre }

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
        ) {
            // ── Header ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Descubre",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "Artistas complementarios a tu ADN sonoro",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }

            // ── Genre filter chips ───────────────────────────────────
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(genres) { genre ->
                    val isSelected = genre == selectedGenre
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected)
                                    Brush.linearGradient(listOf(BlueNeon, PurpleNeon))
                                else Brush.linearGradient(listOf(Surface1, Surface1))
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else Outline1,
                                shape = CircleShape
                            )
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = genre,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) BgDeep else TextMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Artist list ──────────────────────────────────────────
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredArtists) { artist ->
                    ArtistMatchCard(artist)
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun ArtistMatchCard(artist: ArtistMatch) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface1)
            .border(
                width = 1.dp,
                color = Outline1,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = artist.accentColor,
                        spotColor = artist.accentColor
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                artist.accentColor.copy(alpha = 0.30f),
                                artist.accentColor.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .border(1.5.dp, artist.accentColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = artist.name.first().toString().uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = artist.accentColor
                )
            }

            Spacer(Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "${artist.role} · ${artist.genre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(Modifier.height(6.dp))
                // Vibe tag
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(artist.accentColor.copy(alpha = 0.12f))
                        .border(1.dp, artist.accentColor.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = artist.vibe,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = artist.accentColor
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Compatibility + connect button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CompatibilityIndicator(
                    percent = artist.compatibility,
                    color = artist.accentColor
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BlueNeon.copy(alpha = 0.12f))
                        .border(1.dp, BlueNeon.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .clickable { }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Conectar",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = BlueNeon
                    )
                }
            }
        }
    }
}

@Composable
private fun CompatibilityIndicator(percent: Int, color: Color) {
    Box(contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(52.dp)
        ) {
            val strokeW = 4.dp.toPx()
            val radius = (size.minDimension - strokeW) / 2f
            // Background ring
            drawCircle(
                color = Color(0xFF1F2E3A),
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(strokeW)
            )
            // Progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * (percent / 100f),
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    strokeW,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percent%",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050A0F)
@Composable
fun DiscoveryScreenPreview() {
    SoundLinkTheme {
        DiscoveryScreen()
    }
}
