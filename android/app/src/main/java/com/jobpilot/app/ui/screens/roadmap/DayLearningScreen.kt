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
import androidx.compose.ui.graphics.Color
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
                .background(MaterialTheme.colorScheme.background),
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
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.textPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Day ${day.dayNumber}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.textPrimary
                )
                Text(
                    text = day.topic,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.textSecondary,
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
            backgroundColor = MaterialTheme.cardBg
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = day.topic,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.textPrimary
                )

                if (!day.learningObjective.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day.learningObjective,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.textSecondary
                    )
                }

                if (day.subtopics.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Key Concepts to Master:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Orange500
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
                                color = MaterialTheme.textSecondary
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
                    color = MaterialTheme.textPrimary
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
                    color = MaterialTheme.textPrimary
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
                        .background(if (isSelected) Orange500 else MaterialTheme.cardBg)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Orange500 else MaterialTheme.cardBorder,
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
                        color = if (isSelected) Color.White else MaterialTheme.textPrimary,
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
                    color = MaterialTheme.textMuted
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

        Spacer(modifier = Modifier.height(24.dp))

        // AI Curriculum Assistant Section
        var userQuestion by remember { mutableStateOf("") }
        Text(
            text = "AI Curriculum Assistant",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Have doubts on ${day.topic}? Ask your AI assistant for explanations or code hints.",
            fontSize = 11.sp,
            color = Slate500
        )
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = userQuestion,
                    onValueChange = { userQuestion = it },
                    placeholder = { Text("Ask a question about ${day.topic}...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    singleLine = false,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val promptSuggestions = listOf("Explain with example", "Common interview question", "How to practice?")
                    promptSuggestions.forEach { suggestion ->
                        Surface(
                            shape = JobPilotShapes.small,
                            color = Orange50,
                            modifier = Modifier.clickable {
                                userQuestion = "$suggestion for ${day.topic}"
                                viewModel.askCurriculumAssistant(day.topic, "$suggestion for ${day.topic}", day.dayNumber)
                            }
                        ) {
                            Text(
                                text = suggestion,
                                fontSize = 10.sp,
                                color = Orange600,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (userQuestion.isNotBlank()) {
                                viewModel.askCurriculumAssistant(day.topic, userQuestion, day.dayNumber)
                            }
                        },
                        enabled = userQuestion.isNotBlank() && !uiState.isAssistantLoading,
                        shape = JobPilotShapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        if (uiState.isAssistantLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thinking...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask Assistant", fontSize = 12.sp)
                        }
                    }
                }

                if (uiState.assistantAnswer != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = JobPilotShapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Assistant Response", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Orange600)
                                IconButton(onClick = { viewModel.clearAssistantAnswer() }, modifier = Modifier.size(20.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.assistantAnswer ?: "",
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
        backgroundColor = MaterialTheme.cardBg
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Task $taskNumber: ${task.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                fontSize = 12.sp,
                color = MaterialTheme.textSecondary
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
                            color = Orange500
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.expectedOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = MaterialTheme.textPrimary
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
            .background(MaterialTheme.cardBg)
            .border(1.dp, MaterialTheme.cardBorder, JobPilotShapes.small)
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
                    color = MaterialTheme.textPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${resource.source} • ${resource.language}",
                    fontSize = 11.sp,
                    color = MaterialTheme.textMuted
                )
            }
        }

        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(16.dp)
        )
    }
}
