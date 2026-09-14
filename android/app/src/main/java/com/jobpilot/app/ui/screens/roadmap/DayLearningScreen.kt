package com.jobpilot.app.ui.screens.roadmap

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.PracticeTask
import com.jobpilot.app.data.model.RoadmapDay
import com.jobpilot.app.data.model.RoadmapResource
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@Composable
fun DayLearningScreen(
    roadmapId: String,
    dayId: String,
    viewModel: RoadmapViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Find day from loaded roadmap
    val currentRoadmap = uiState.currentRoadmap
    var activeDay by remember { mutableStateOf<RoadmapDay?>(null) }

    LaunchedEffect(currentRoadmap, dayId) {
        if (currentRoadmap != null) {
            for (phase in currentRoadmap.phases) {
                val found = phase.days.firstOrNull { it.id == dayId }
                if (found != null) {
                    activeDay = found
                    viewModel.loadPhaseResources(found.phaseId, uiState.selectedLanguage)
                    break
                }
            }
        } else {
            viewModel.loadRoadmapDetail(roadmapId)
        }
    }

    val day = activeDay

    if (day == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgWarmWhite),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Orange500)
        }
        return
    }

    val languages = listOf("English", "Telugu", "Hindi")

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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Day ${day.dayNumber}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = day.topic,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600,
                    maxLines = 1
                )
            }
            if (day.isCompleted) {
                Box(
                    modifier = Modifier
                        .clip(JobPilotShapes.small)
                        .background(SuccessGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Completed",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic & Objective Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = day.topic,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                if (!day.learningObjective.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day.learningObjective,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate600
                    )
                }

                if (day.subtopics.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Key Concepts to Master:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    day.subtopics.forEach { subtopic ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "• ",
                                color = Orange500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = subtopic,
                                fontSize = 13.sp,
                                color = Slate700
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Practice Programs Section
        if (day.practiceTasks.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = Orange500,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hands-on Practice Tasks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            day.practiceTasks.forEachIndexed { idx, task ->
                PracticeTaskCard(taskNumber = idx + 1, task = task)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Learning Resources & Video Links Section with 3-Language Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = null,
                    tint = Orange500,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Curated Resources & Videos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Language Tabs: English, Telugu, Hindi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { lang ->
                val isSelected = lang == uiState.selectedLanguage
                val label = when (lang) {
                    "Telugu" -> "తెలుగు (Telugu)"
                    "Hindi" -> "हिन्दी (Hindi)"
                    else -> "English"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(JobPilotShapes.small)
                        .background(if (isSelected) Orange500 else BgWhite)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Orange500 else Slate200,
                            shape = JobPilotShapes.small
                        )
                        .clickable {
                            viewModel.changeResourceLanguage(day.phaseId, lang)
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BgWhite else Slate700,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Resources list for active language
        if (uiState.phaseResources.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading curated ${uiState.selectedLanguage} resources...",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        } else {
            uiState.phaseResources.forEach { resource ->
                ResourceItemRow(
                    resource = resource,
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Complete Day Button
        if (!day.isCompleted) {
            JobPilotButton(
                text = if (uiState.isLoading) "Saving Progress..." else "✓ Mark Day as Complete",
                onClick = {
                    viewModel.completeDay(day.id) {
                        // Successfully completed
                        activeDay = activeDay?.copy(isCompleted = true)
                    }
                },
                enabled = !uiState.isLoading
            )
        } else {
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth(),
                shape = JobPilotShapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SuccessGreen)
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Day Completed • Return to Roadmap")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PracticeTaskCard(
    taskNumber: Int,
    task: PracticeTask
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = BgWhite
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Task $taskNumber: ${task.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                fontSize = 12.sp,
                color = Slate600
            )
            if (!task.expectedOutput.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(JobPilotShapes.small)
                        .background(Slate100)
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Expected Output / Result:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.expectedOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Slate800
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourceItemRow(
    resource: RoadmapResource,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(JobPilotShapes.small)
            .background(BgWhite)
            .border(1.dp, Slate200, JobPilotShapes.small)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (resource.resourceType == "video") Icons.Default.VideoLibrary else Icons.Default.MenuBook,
                contentDescription = null,
                tint = if (resource.resourceType == "video") ErrorRed else Orange500,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = resource.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Slate900,
                    maxLines = 1
                )
                Text(
                    text = "${resource.source} • ${resource.language}",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }
        }

        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(16.dp)
        )
    }
}
