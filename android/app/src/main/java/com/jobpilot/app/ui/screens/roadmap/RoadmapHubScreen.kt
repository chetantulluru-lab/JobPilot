package com.jobpilot.app.ui.screens.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.RoadmapSummary
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@Composable
fun RoadmapHubScreen(
    viewModel: RoadmapViewModel,
    onCreateRoadmap: () -> Unit,
    onSelectRoadmap: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRoadmaps()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRoadmap,
                containerColor = Orange500,
                contentColor = BgWhite
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Roadmap")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Career Roadmaps",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.textPrimary
                    )
                    Text(
                        text = "Structured day-by-day AI learning plans",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.textSecondary
                    )
                }
                AIOrb(size = 44.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.isLoading && uiState.roadmaps.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Orange500)
                }
            } else if (uiState.roadmaps.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        AIOrb(size = 88.dp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "No Roadmaps Generated Yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.textPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pick a career goal or target technology to generate your structured 3, 6, or 12 month day-by-day learning curriculum.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.textSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        JobPilotButton(
                            text = "+ Create Your First Roadmap",
                            onClick = onCreateRoadmap
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(uiState.roadmaps, key = { it.id }) { roadmap ->
                        RoadmapCard(
                            roadmap = roadmap,
                            onClick = { onSelectRoadmap(roadmap.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoadmapCard(
    roadmap: RoadmapSummary,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = MaterialTheme.cardBg
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = roadmap.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Goal: ${roadmap.goal}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.textSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(JobPilotShapes.small)
                        .background(Orange50)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = roadmap.duration,
                        color = Orange500,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${roadmap.completedDays}/${roadmap.totalDays} Days Completed",
                    fontSize = 12.sp,
                    color = MaterialTheme.textSecondary
                )
                Text(
                    text = "${roadmap.progressPercentage}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (roadmap.isCompleted) SuccessGreen else Orange500
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { roadmap.progressPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(JobPilotShapes.small),
                color = if (roadmap.isCompleted) SuccessGreen else Orange500,
                trackColor = MaterialTheme.cardBorder
            )

            if (roadmap.currentDayTopic != null && !roadmap.isCompleted) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = MaterialTheme.textMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Current: ${roadmap.currentDayTopic}",
                        fontSize = 12.sp,
                        color = MaterialTheme.textSecondary,
                        maxLines = 1
                    )
                }
            } else if (roadmap.isCompleted) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Roadmap Completed 🎉",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreen
                    )
                }
            }
        }
    }
}
