package com.example.soundlink.features.activities.ui.screens.activitydescription

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.soundlink.R
import com.example.soundlink.app.theme.SoundLinkTheme
import com.example.soundlink.core.ui.components.NeonButton

enum class ActivityDetailSection(val displayName: String) {
    General("Descripción"),
    Rules("Reglas"),
    Prizes("Premios")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ActivityDescriptionScreen(
    state: ActivityDescriptionState,
    onSectionSelected: (ActivityDetailSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            // Imagen de portada con animación y sombreado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = state.activity.imageRes),
                    contentDescription = state.activity.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 45.dp,
                                bottomEnd = 45.dp
                            )
                        )
                )
            }

            // Minimenu
            Spacer(Modifier.height(22.dp))
            MiniNavBar(
                current = state.selectedSection,
                onSectionSelected = onSectionSelected,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(18.dp))

            // Animated Content
            AnimatedContent(
                targetState = state.selectedSection,

                transitionSpec = {
                    (fadeIn(animationSpec = tween(420)) + slideInVertically()) togetherWith
                            fadeOut(animationSpec = tween(250))
                },
                label = "ActivityDetailsAnim"
            ) { section ->
                when (section) {
                    ActivityDetailSection.General -> {
                        DescriptionSection(
                            name = state.activity.name,
                            description = state.activity.description
                        )
                    }
                    ActivityDetailSection.Rules -> {
                        RulesSection(rules = state.activity.rules)
                    }
                    ActivityDetailSection.Prizes -> {
                        PrizesSection(prizes = state.activity.prizes)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            NeonButton(onClick = { /* TODO */ }, text = "Participar")

        }
    }
}

@Composable
fun MiniNavBar(
    current: ActivityDetailSection,
    onSectionSelected: (ActivityDetailSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val sections = ActivityDetailSection.values()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.76f))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        sections.forEach { section ->
            val selected = section == current
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
                        else Color.Transparent
                    )
                    .clickable { onSectionSelected(section) }
                    .padding(horizontal = 18.dp, vertical = 7.dp)
            ) {
                Text(
                    section.displayName,
                    fontSize = 15.sp,
                    color = if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.69f),
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun DescriptionSection(
    name: String,
    description: String
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun RulesSection(rules: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Reglas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
        )
        rules.forEachIndexed { idx, rule ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = rule,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@Composable
fun PrizesSection(prizes: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Premios",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
        )
        prizes.forEachIndexed { idx, prize ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = prize,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ActivityDescriptionScreenPreview() {
    SoundLinkTheme {
        val fakeActivity = ActivityDetail(
            imageRes = R.drawable.activity1,
            name = "Relajación Sonora",
            description = "Sumérgete en una experiencia de relajación profunda utilizando sonidos suaves y de la naturaleza. Perfecto para disminuir el estrés y mejorar tu bienestar emocional.",
            rules = listOf("Encuentra un lugar cómodo", "Usa auriculares para mejor calidad", "Cierra los ojos y concéntrate en la respiración"),
            prizes = listOf("Medalla de relajación", "Reconocimiento en el perfil", "Acceso a actividades exclusivas")
        )
        val state = ActivityDescriptionState(
            activity = fakeActivity,
            selectedSection = ActivityDetailSection.General
        )
        ActivityDescriptionScreen(
            state = state,
            onSectionSelected = {},
        )
    }
}