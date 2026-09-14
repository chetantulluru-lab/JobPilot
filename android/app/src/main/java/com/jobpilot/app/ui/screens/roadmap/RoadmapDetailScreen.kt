package com.jobpilot.app.ui.screens.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.jobpilot.app.data.model.RoadmapDay
import com.jobpilot.app.data.model.RoadmapPhase
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@Composable
fun RoadmapDetailScreen(
    roadmapId: String,
    viewModel: RoadmapViewModel,
    onNavigateBack: () -> Unit,
    onOpenDay: (String, String) -> Unit // roadmapId, dayId
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(roadmapId) {
        viewModel.loadRoadmapDetail(roadmapId)
    }

    val roadmap = uiState.currentRoadmap

    if (roadmap == null) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.textPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = roadmap.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.textPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${roadmap.duration} • ${roadmap.completedDays}/${roadmap.totalDays} Days Done",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.textSecondary
                )
            }
            AIOrb(size = 38.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Overview Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.cardBg
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curriculum Progress",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.textPrimary
                    )
                    Text(
                        text = "${roadmap.progressPercentage}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (roadmap.isCompleted) SuccessGreen else Orange500
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { roadmap.progressPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(JobPilotShapes.small),
                    color = if (roadmap.isCompleted) SuccessGreen else Orange500,
                    trackColor = MaterialTheme.cardBorder
                )

                if (roadmap.skillsLearned.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Target Skills: " + roadmap.skillsLearned.take(5).joinToString(", "),
                        fontSize = 12.sp,
                        color = MaterialTheme.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Phases & Days List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            items(roadmap.phases, key = { it.id }) { phase ->
                PhaseCard(
                    phase = phase,
                    onOpenDay = { dayId -> onOpenDay(roadmap.id, dayId) }
                )
            }
        }
    }

    // Roadmap Completed Skill Transfer Modal
    if (uiState.roadmapCompletedEvent) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRoadmapCompletedModal() },
            icon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Orange500,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Roadmap Completed!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Congratulations! You have finished every day and project in this curriculum.",
                        textAlign = TextAlign.Center,
                        color = Slate600,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Would you like to automatically transfer these learned skills into your Career Profile and ATS Resume?",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = Slate800,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addSkillsToResume(roadmap.id) {
                            viewModel.dismissRoadmapCompletedModal()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Add Skills to Profile", color = BgWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissRoadmapCompletedModal() }) {
                    Text("Not Now", color = Slate600)
                }
            }
        )
    }

    // Success snackbar for skill addition
    if (uiState.skillsAddedMessage != null) {
        LaunchedEffect(uiState.skillsAddedMessage) {
            // Keep notification visible briefly
        }
    }
}

@Composable
private fun PhaseCard(
    phase: RoadmapPhase,
    onOpenDay: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(phase.isUnlocked) }
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.cardBg
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (phase.isUnlocked) expanded = !expanded
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(JobPilotShapes.small)
                            .background(
                                when {
                                    phase.isCompleted -> SuccessGreen.copy(alpha = 0.15f)
                                    phase.isUnlocked -> Orange50
                                    else -> Slate100
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                phase.isCompleted -> Icons.Default.Check
                                phase.isUnlocked -> Icons.Default.LockOpen
                                else -> Icons.Default.Lock
                            },
                            contentDescription = null,
                            tint = when {
                                phase.isCompleted -> SuccessGreen
                                phase.isUnlocked -> Orange500
                                else -> MaterialTheme.textMuted
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Phase ${phase.phaseNumber}: ${phase.title}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (phase.isUnlocked) MaterialTheme.textPrimary else MaterialTheme.textMuted
                        )
                        if (!phase.isUnlocked) {
                            Text(
                                text = "Complete previous phase to unlock",
                                fontSize = 11.sp,
                                color = MaterialTheme.textMuted
                            )
                        } else {
                            val completedCount = phase.days.count { it.isCompleted }
                            Text(
                                text = "$completedCount/${phase.days.size} Days Finished",
                                fontSize = 11.sp,
                                color = MaterialTheme.textSecondary
                            )
                        }
                    }
                }

                if (phase.isUnlocked) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.textMuted
                    )
                }
            }

            if (phase.isUnlocked && expanded) {
                if (!phase.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = phase.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = MaterialTheme.cardBorder)
                Spacer(modifier = Modifier.height(10.dp))

                // Days list
                phase.days.forEach { day ->
                    DayRowItem(
                        day = day,
                        onClick = { onOpenDay(day.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Phase Project
                if (!phase.projectTitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    PhaseProjectBadge(
                        title = phase.projectTitle,
                        description = phase.projectDescription
                    )
                }
            }
        }
    }
}

@Composable
private fun DayRowItem(
    day: RoadmapDay,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(JobPilotShapes.small)
            .background(
                if (day.isCompleted) SuccessGreenBg
                else BgWhite
            )
            .border(
                1.dp,
                if (day.isCompleted) SuccessGreen.copy(alpha = 0.4f) else Slate200,
                JobPilotShapes.small
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (day.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (day.isCompleted) SuccessGreen else MaterialTheme.textMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Day ${day.dayNumber}: ${day.topic}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.textPrimary
                )
                if (!day.learningObjective.isNullOrBlank()) {
                    Text(
                        text = day.learningObjective,
                        fontSize = 11.sp,
                        color = MaterialTheme.textSecondary,
                        maxLines = 1
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun PhaseProjectBadge(
    title: String,
    description: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(JobPilotShapes.medium)
            .background(Orange50)
            .border(1.dp, Orange200, JobPilotShapes.medium)
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    tint = Orange500,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Phase Capstone: $title",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Orange500
                )
            }
            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = MaterialTheme.textSecondary
                )
            }
        }
    }
}
