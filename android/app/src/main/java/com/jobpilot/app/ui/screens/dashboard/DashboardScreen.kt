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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.ApplicationStatus
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToSmartCompletion: () -> Unit,
    onNavigateToJobs: () -> Unit,
    onNavigateToJobDetail: (String) -> Unit,
    onNavigateToApplications: () -> Unit,
    onNavigateToApplicationDetail: (String) -> Unit,
    onNavigateToResume: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAssistant: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()

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
                        text = "Good morning, ${uiState.userName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    Text(
                        text = "Your Career Cockpit",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Slate900
                    )
                }

                IconButton(
                    onClick = onNavigateToNotifications,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Orange50)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Orange500
                    )
                }
            }
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Cockpit Metrics Grid (Profile Strength, Job Matches, Applications, Interviews)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Profile Strength Card
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = BgWhite,
                        onClick = onNavigateToProfile
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Profile Strength",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Slate500
                                )
                                Text(
                                    text = "${uiState.profileStrength}%",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Orange600,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { uiState.profileStrength / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = Orange500,
                                trackColor = Orange50
                            )
                        }
                    }

                    // Job Matches Card
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = BgWhite,
                        onClick = onNavigateToJobs
                    ) {
                        Column {
                            Text(
                                text = "Job Matches",
                                style = MaterialTheme.typography.labelMedium,
                                color = Slate500
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${uiState.totalJobMatches}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Slate900
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Applications Card
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = BgWhite,
                        onClick = onNavigateToApplications
                    ) {
                        Column {
                            Text(
                                text = "Applications",
                                style = MaterialTheme.typography.labelMedium,
                                color = Slate500
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${uiState.totalApplications}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Slate900
                            )
                        }
                    }

                    // Interviews Card
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = BgWhite,
                        onClick = onNavigateToApplications
                    ) {
                        Column {
                            Text(
                                text = "Interviews",
                                style = MaterialTheme.typography.labelMedium,
                                color = Slate500
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${uiState.totalInterviews}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }

            // 2. Main AI Insight Card (with prominent "Improve My Profile" action)
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Orange50.copy(alpha = 0.7f),
                    borderColor = Orange300.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        AIOrb(size = 54.dp, showRings = false)

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AI CAREER INSIGHT",
                                    style = JobPilotTypography.labelMedium,
                                    color = Orange600,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "✦", color = Orange500, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = uiState.aiInsight,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate800,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = onNavigateToSmartCompletion,
                                    colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                                    shape = ButtonShape,
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Improve Profile",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = onNavigateToAssistant,
                                    shape = ButtonShape,
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Orange500)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Orange600
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Chat AI Coach",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Orange600
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }

            // 3. Recommended Jobs Section
            item {
                SectionHeader(
                    title = "Recommended For You",
                    subtitle = "High-affinity semantic matches",
                    actionText = "View All",
                    onActionClick = onNavigateToJobs
                )
            }

            items(uiState.recommendedJobs) { job ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite,
                    onClick = { onNavigateToJobDetail(job.id) }
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = job.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Text(
                                    text = "${job.company} • ${job.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            MatchScoreBadge(score = job.matchDetails.matchScore)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.matchDetails.strongMatches.take(3).forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.STRONG_MATCH)
                            }
                        }
                    }
                }
            }

            // 4. Skill Gaps Overview Card
            item {
                SectionHeader(
                    title = "Priority Skill Gaps",
                    subtitle = "Master these to unlock higher match rates"
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Identified across your target roles:",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            uiState.topSkillGaps.forEach { gap ->
                                SkillChip(skillName = gap, variant = ChipVariant.MISSING_GAP)
                            }
                        }
                    }
                }
            }

            // 5. Resume Status Card
            item {
                SectionHeader(
                    title = "Resume Status",
                    actionText = "Open Builder",
                    onActionClick = onNavigateToResume
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite,
                    onClick = onNavigateToResume
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = uiState.resumeStatusText,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Text(
                                    text = "Ready to export or tailor for jobs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Slate400
                        )
                    }
                }
            }

            // 6. Recent Applications Section
            item {
                SectionHeader(
                    title = "Recent Applications",
                    actionText = "Pipeline",
                    onActionClick = onNavigateToApplications
                )
            }

            items(uiState.recentApplications) { app ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite,
                    onClick = { onNavigateToApplicationDetail(app.id) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = app.jobTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate900
                            )
                            Text(
                                text = "${app.company} • ${app.appliedDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }

                        Surface(
                            shape = ChipShape,
                            color = when (app.currentStatus) {
                                ApplicationStatus.INTERVIEW -> InfoBlueBg
                                ApplicationStatus.ASSESSMENT -> WarningAmberBg
                                ApplicationStatus.OFFER -> SuccessGreenBg
                                else -> Slate100
                            }
                        ) {
                            Text(
                                text = app.currentStatus.displayName,
                                style = JobPilotTypography.labelMedium,
                                color = when (app.currentStatus) {
                                    ApplicationStatus.INTERVIEW -> InfoBlue
                                    ApplicationStatus.ASSESSMENT -> WarningAmber
                                    ApplicationStatus.OFFER -> SuccessGreen
                                    else -> Slate700
                                },
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
