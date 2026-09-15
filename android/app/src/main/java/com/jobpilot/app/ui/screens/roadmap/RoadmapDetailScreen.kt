package com.jobpilot.app.ui.screens.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

    // Find current active day (first uncompleted day)
    val allDays = remember(roadmap) {
        roadmap.phases.flatMap { it.days }.sortedBy { it.dayNumber }
    }
    val activeDay = remember(allDays) {
        allDays.firstOrNull { !it.isCompleted } ?: allDays.lastOrNull()
    }

    // Active selected phase tab index
    val defaultPhaseIndex = remember(roadmap, activeDay) {
        val activePhaseId = activeDay?.phaseId
        val idx = roadmap.phases.indexOfFirst { it.id == activePhaseId }
        if (idx >= 0) idx else 0
    }
    var selectedPhaseIndex by remember { mutableIntStateOf(defaultPhaseIndex) }

    // Ensure phase index stays valid
    val currentPhaseIndex = selectedPhaseIndex.coerceIn(0, (roadmap.phases.size - 1).coerceAtLeast(0))
    val selectedPhase = if (roadmap.phases.isNotEmpty()) roadmap.phases[currentPhaseIndex] else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Top Bar ---
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = roadmap.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.textPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "${roadmap.duration} • ${roadmap.completedDays}/${roadmap.totalDays} Days Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.textSecondary
                    )
                }
                AIOrb(size = 36.dp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
        ) {
            // --- Progress Card ---
            item {
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
                                text = "Curriculum Completion",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
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
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Target Skills: " + roadmap.skillsLearned.take(5).joinToString(", "),
                                fontSize = 11.sp,
                                color = MaterialTheme.textSecondary
                            )
                        }
                    }
                }
            }

            // --- Active Day Focus Card ---
            if (activeDay != null && !roadmap.isCompleted) {
                item {
                    Surface(
                        shape = JobPilotShapes.medium,
                        color = Orange50,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Orange300),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDay(roadmap.id, activeDay.id) }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircleFilled,
                                        contentDescription = null,
                                        tint = Orange500,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "TODAY'S LESSON",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Orange600,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Surface(
                                    shape = JobPilotShapes.small,
                                    color = Orange500
                                ) {
                                    Text(
                                        text = "Day ${activeDay.dayNumber}",
                                        color = BgWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = activeDay.topic,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.textPrimary
                            )

                            if (!activeDay.learningObjective.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeDay.learningObjective,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.textSecondary,
                                    maxLines = 2
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { onOpenDay(roadmap.id, activeDay.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                                shape = JobPilotShapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = BgWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Continue Day ${activeDay.dayNumber} Lesson",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BgWhite
                                )
                            }
                        }
                    }
                }
            }

            // --- Phase Navigation Tabs ---
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Curriculum Phases",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.textPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(roadmap.phases) { idx, phase ->
                            val isSelected = idx == currentPhaseIndex
                            val completedCount = phase.days.count { it.isCompleted }
                            val isPhaseDone = phase.isCompleted || (phase.days.isNotEmpty() && completedCount == phase.days.size)

                            Surface(
                                shape = JobPilotShapes.medium,
                                color = when {
                                    isSelected -> Orange500
                                    isPhaseDone -> SuccessGreenBg
                                    phase.isUnlocked -> MaterialTheme.cardBg
                                    else -> Slate100
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    when {
                                        isSelected -> Orange500
                                        isPhaseDone -> SuccessGreen.copy(alpha = 0.4f)
                                        phase.isUnlocked -> MaterialTheme.cardBorder
                                        else -> Slate200
                                    }
                                ),
                                modifier = Modifier
                                    .clickable { selectedPhaseIndex = idx }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when {
                                            isPhaseDone -> Icons.Default.CheckCircle
                                            phase.isUnlocked -> Icons.Default.FolderOpen
                                            else -> Icons.Default.Lock
                                        },
                                        contentDescription = null,
                                        tint = when {
                                            isSelected -> BgWhite
                                            isPhaseDone -> SuccessGreen
                                            phase.isUnlocked -> Orange500
                                            else -> MaterialTheme.textMuted
                                        },
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Column {
                                        Text(
                                            text = "Phase ${phase.phaseNumber}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (isSelected) BgWhite else MaterialTheme.textPrimary
                                        )
                                        Text(
                                            text = "$completedCount/${phase.days.size} Days",
                                            fontSize = 10.sp,
                                            color = if (isSelected) BgWhite.copy(alpha = 0.8f) else MaterialTheme.textSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- Selected Phase Header & Days Timeline ---
            if (selectedPhase != null) {
                item {
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
                                    text = "Phase ${selectedPhase.phaseNumber}: ${selectedPhase.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.textPrimary
                                )

                                if (selectedPhase.isUnlocked) {
                                    Surface(
                                        shape = JobPilotShapes.small,
                                        color = Orange50
                                    ) {
                                        Text(
                                            text = "${selectedPhase.days.count { it.isCompleted }}/${selectedPhase.days.size} Completed",
                                            color = Orange600,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            if (!selectedPhase.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = selectedPhase.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.textSecondary
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Day-by-Day Learning Path",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.textPrimary
                    )
                }

                // Days list for selected phase
                items(selectedPhase.days, key = { it.id }) { day ->
                    val isDayActive = day.id == activeDay?.id
                    DayTimelineCard(
                        day = day,
                        isActive = isDayActive,
                        isPhaseUnlocked = selectedPhase.isUnlocked,
                        onClick = {
                            if (selectedPhase.isUnlocked) {
                                onOpenDay(roadmap.id, day.id)
                            }
                        }
                    )
                }

                // Phase Capstone Card
                if (!selectedPhase.projectTitle.isNullOrBlank()) {
                    item {
                        Surface(
                            shape = JobPilotShapes.medium,
                            color = Orange50,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Orange200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Orange500,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Phase Capstone: ${selectedPhase.projectTitle}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Orange600
                                    )
                                }
                                if (!selectedPhase.projectDescription.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = selectedPhase.projectDescription,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.textSecondary
                                    )
                                }
                            }
                        }
                    }
                }
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
}

/**
 * Clean step card representing a single day in the day-by-day roadmap sequence.
 */
@Composable
private fun DayTimelineCard(
    day: RoadmapDay,
    isActive: Boolean,
    isPhaseUnlocked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = JobPilotShapes.medium,
        color = when {
            day.isCompleted -> SuccessGreenBg
            isActive -> Orange50
            isPhaseUnlocked -> MaterialTheme.cardBg
            else -> Slate100
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                day.isCompleted -> SuccessGreen.copy(alpha = 0.4f)
                isActive -> Orange400
                isPhaseUnlocked -> MaterialTheme.cardBorder
                else -> Slate200
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isPhaseUnlocked, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Step Indicator Badge
                Surface(
                    shape = JobPilotShapes.small,
                    color = when {
                        day.isCompleted -> SuccessGreen
                        isActive -> Orange500
                        isPhaseUnlocked -> MaterialTheme.cardBorder
                        else -> Slate200
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (day.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = BgWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${day.dayNumber}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isActive) BgWhite else MaterialTheme.textPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Day ${day.dayNumber}: ${day.topic}",
                            fontSize = 13.sp,
                            fontWeight = if (isActive || day.isCompleted) FontWeight.Bold else FontWeight.Medium,
                            color = if (isPhaseUnlocked) MaterialTheme.textPrimary else MaterialTheme.textMuted
                        )
                    }

                    if (!day.learningObjective.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = day.learningObjective,
                            fontSize = 11.sp,
                            color = MaterialTheme.textSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            // Right Action Indicator
            Icon(
                imageVector = when {
                    day.isCompleted -> Icons.Default.CheckCircle
                    isActive -> Icons.Default.PlayCircleFilled
                    isPhaseUnlocked -> Icons.Default.ChevronRight
                    else -> Icons.Default.Lock
                },
                contentDescription = null,
                tint = when {
                    day.isCompleted -> SuccessGreen
                    isActive -> Orange500
                    isPhaseUnlocked -> MaterialTheme.textMuted
                    else -> Slate400
                },
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
