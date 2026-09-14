package com.jobpilot.app.ui.screens.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoadmapCreateScreen(
    viewModel: RoadmapViewModel,
    onNavigateBack: () -> Unit,
    onRoadmapCreated: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var goalText by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf("6 Months") }

    val durations = listOf("3 Months", "6 Months", "12 Months")
    val popularSuggestions = listOf(
        "Python Backend Developer",
        "React Frontend Engineer",
        "Data Scientist",
        "Full Stack Web Developer",
        "FastAPI Microservices",
        "DevOps & Cloud Engineer",
        "Android Developer with Jetpack Compose",
        "NLP & Large Language Models"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Slate700)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Generate AI Roadmap",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card with Orb
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AIOrb(size = 56.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Curriculum Architect",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Generates structured phases, daily topics, practice programs & multilingual resources.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Goal Input Field with Live Autocomplete
        Text(
            text = "Target Role or Technology",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Slate800
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = goalText,
            onValueChange = {
                goalText = it
                viewModel.onQueryChanged(it)
            },
            placeholder = { Text("e.g. Python Backend Developer, NLP, Cloud Architect") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Slate400)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = JobPilotShapes.medium,
            singleLine = true
        )

        // Autocomplete suggestions chips (Live from backend or popular defaults)
        val activeSuggestions = if (uiState.searchSuggestions.isNotEmpty()) {
            uiState.searchSuggestions
        } else if (goalText.isEmpty()) {
            popularSuggestions
        } else {
            emptyList()
        }

        if (activeSuggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (uiState.searchSuggestions.isNotEmpty()) "Suggested Matches:" else "Popular Career Paths:",
                fontSize = 12.sp,
                color = Slate500,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                activeSuggestions.take(6).forEach { suggestion ->
                    SuggestionChip(
                        onClick = {
                            goalText = suggestion
                            viewModel.onQueryChanged(suggestion)
                        },
                        label = { Text(suggestion, fontSize = 12.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Orange50,
                            labelColor = Orange600
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = Orange200
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Duration Picker
        Text(
            text = "Target Learning Duration",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Slate800
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            durations.forEach { duration ->
                val isSelected = duration == selectedDuration
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(JobPilotShapes.medium)
                        .background(if (isSelected) Orange500 else BgWhite)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Orange500 else Slate200,
                            shape = JobPilotShapes.medium
                        )
                        .clickable { selectedDuration = duration }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = duration,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        color = if (isSelected) BgWhite else Slate700
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Generate Action Button
        JobPilotButton(
            text = if (uiState.isGenerating) "Designing Your Curriculum..." else "Generate Career Roadmap",
            onClick = {
                if (goalText.isNotBlank()) {
                    viewModel.generateRoadmap(
                        goal = goalText.trim(),
                        duration = selectedDuration,
                        onSuccess = { roadmapId ->
                            onRoadmapCreated(roadmapId)
                        }
                    )
                }
            },
            enabled = !uiState.isGenerating && goalText.trim().length >= 2
        )

        // Honest Error Dialog if OpenRouter or structured output fails
        if (uiState.errorMessage != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(36.dp)
                    )
                },
                title = {
                    Text(
                        text = "Generation Unsuccessful",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        text = uiState.errorMessage ?: "Roadmap couldn't be generated. Please try again.",
                        textAlign = TextAlign.Center,
                        color = Slate600
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearError()
                            viewModel.generateRoadmap(
                                goal = goalText.trim(),
                                duration = selectedDuration,
                                onSuccess = { roadmapId ->
                                    onRoadmapCreated(roadmapId)
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Retry", color = BgWhite)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Cancel", color = Slate600)
                    }
                }
            )
        }
    }
}
