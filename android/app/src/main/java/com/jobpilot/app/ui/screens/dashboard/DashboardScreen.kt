package com.jobpilot.app.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.RoadmapSummary
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToRoadmaps: () -> Unit,
    onNavigateToRoadmapDetail: (String) -> Unit,
    onCreateRoadmap: () -> Unit,
    onNavigateToResume: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAssistant: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWarmWhite)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (uiState.userName.isNotBlank()) "Welcome, ${uiState.userName}" else "Welcome to JobPilot",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    Text(
                        text = "Career Cockpit",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Slate900,
                        fontWeight = FontWeight.Bold
                    )
                }

                AIOrb(
                    size = 44.dp,
                    modifier = Modifier.clickable { onNavigateToAssistant() }
                )
            }
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Metric Cards: Streak & Profile Strength
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Daily Streak Card
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = BgWhite
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Learning Streak",
                                    fontSize = 12.sp,
                                    color = Slate500,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(text = "🔥", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${uiState.currentStreak} Days",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Best: ${uiState.longestStreak} days",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    // Profile Strength Card
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onNavigateToProfile),
                        backgroundColor = BgWhite
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Profile Score",
                                    fontSize = 12.sp,
                                    color = Slate500,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${uiState.profileStrength}%",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.profileStrength >= 80) SuccessGreen else Orange500
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tap to update profile",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }
                }
            }

            // 2. Active Roadmap Card
            item {
                val active = uiState.activeRoadmap
                if (active != null) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = BgWhite
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(JobPilotShapes.small)
                                        .background(Orange50)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE ROADMAP • ${active.duration}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Orange600
                                    )
                                }

                                Text(
                                    text = "${active.progressPercentage}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active.isCompleted) SuccessGreen else Orange500
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = active.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Goal: ${active.goal}",
                                fontSize = 12.sp,
                                color = Slate600
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = { active.progressPercentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(JobPilotShapes.small),
                                color = if (active.isCompleted) SuccessGreen else Orange500,
                                trackColor = Slate200
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${active.completedDays} of ${active.totalDays} days completed",
                                fontSize = 11.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            JobPilotButton(
                                text = if (active.isCompleted) "Review Roadmap" else "Continue Learning →",
                                onClick = { onNavigateToRoadmapDetail(active.id) }
                            )
                        }
                    }
                } else {
                    // Empty Roadmap Prompt
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = BgWhite
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AIOrb(size = 56.dp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Active Roadmap",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Generate a day-by-day structured curriculum with multilingual practice tasks.",
                                fontSize = 12.sp,
                                color = Slate500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            JobPilotButton(
                                text = "+ Create Career Roadmap",
                                onClick = onCreateRoadmap
                            )
                        }
                    }
                }
            }

            // 3. AI Coach Spotlight Card
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToAssistant),
                    backgroundColor = BgWhite
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AIOrb(size = 54.dp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AI Career Coach",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Instant guidance on skills, interview questions, and ATS resume improvements.",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Slate400
                        )
                    }
                }
            }

            // 4. Active Roadmaps List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Roadmaps",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    TextButton(onClick = onCreateRoadmap) {
                        Text(
                            text = "+ New Roadmap",
                            color = Orange500,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // 5. Roadmaps Horizontal Scroll or Empty
            if (uiState.roadmaps.isNotEmpty()) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.roadmaps, key = { it.id }) { summary ->
                            MiniRoadmapCard(
                                summary = summary,
                                onClick = { onNavigateToRoadmapDetail(summary.id) }
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "No roadmaps created yet. Tap '+ New Roadmap' above to get started.",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MiniRoadmapCard(
    summary: RoadmapSummary,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        backgroundColor = BgWhite
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = summary.duration,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Orange500
                )
                Text(
                    text = "${summary.progressPercentage}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (summary.isCompleted) SuccessGreen else Orange500
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = summary.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Slate900,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = summary.goal,
                fontSize = 11.sp,
                color = Slate500,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { summary.progressPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(JobPilotShapes.small),
                color = if (summary.isCompleted) SuccessGreen else Orange500,
                trackColor = Slate200
            )
        }
    }
}
