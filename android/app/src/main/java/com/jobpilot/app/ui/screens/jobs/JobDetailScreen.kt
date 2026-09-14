package com.jobpilot.app.ui.screens.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    jobId: String,
    viewModel: JobViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSkillGap: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(jobId) {
        viewModel.selectJobById(jobId)
    }

    val job = uiState.selectedJob

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(job?.title ?: "Job Details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        if (job == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingState(message = "Retrieving job parameters...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Details
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${job.company} • ${job.location} • ${job.workMode}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                        Text(
                            text = "${job.employmentType} • ${job.stipendOrSalary}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange600,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Match Score Card with Ring
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = BgWhite
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "AI Match Compatibility",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Slate500
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (job.matchDetails.matchScore != null) {
                                    Text(
                                        text = "${job.matchDetails.matchScore}% Match",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = Slate900
                                    )
                                    Text(
                                        text = job.matchDetails.matchTier?.name?.replace("_", " ") ?: "EVALUATED",
                                        style = JobPilotTypography.labelLarge,
                                        color = Orange600
                                    )
                                } else {
                                    Text(
                                        text = "Match Unavailable",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Slate800,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Complete your Career Profile or upload your resume to calculate AI alignment.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate500
                                    )
                                }
                            }
                            MatchScoreRing(score = job.matchDetails.matchScore, size = 70.dp)
                        }
                    }
                }

                // AI "Why This Matches You" Card
                if (job.matchDetails.whyItMatchesExplanation.isNotBlank()) {
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Orange50.copy(alpha = 0.7f),
                            borderColor = Orange300.copy(alpha = 0.6f)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Why this matches you",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Orange700,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = job.matchDetails.whyItMatchesExplanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate800,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // Skills Breakdown
                item {
                    if (job.matchDetails.strongMatches.isNotEmpty()) {
                        SectionHeader(title = "Strong Matches (${job.matchDetails.strongMatches.size})")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.matchDetails.strongMatches.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.STRONG_MATCH)
                            }
                        }
                    } else {
                        SectionHeader(title = "Required Skills (${job.requirements.size})")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.requirements.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.NEUTRAL)
                            }
                        }
                    }
                }

                // Missing / Weak Skills Breakdown
                item {
                    SectionHeader(title = "Potential Skill Gaps (${job.matchDetails.missingSkills.size})")
                    if (job.matchDetails.missingSkills.isEmpty()) {
                        Text(text = "None! You possess all primary required skills.", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.matchDetails.missingSkills.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.MISSING_GAP)
                            }
                        }
                    }
                }

                // If match is moderate or developing, provide direct button to view Skill Gap learning plan!
                if (job.matchDetails.improvementPlan.isNotEmpty()) {
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = GapOrangeBg,
                            borderColor = GapOrange.copy(alpha = 0.3f),
                            onClick = { onNavigateToSkillGap(job.id) }
                        ) {
                            Column {
                                Text(
                                    text = "Skill Gap Improvement Plan Available",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GapOrange,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tap to see 4 actionable steps to boost your match from ${job.matchDetails.matchScore}% to 90%+.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate700
                                )
                            }
                        }
                    }
                }

                // Full Job Requirements Checklist
                item {
                    SectionHeader(title = "Job Requirements")
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            job.requirements.forEach { req ->
                                val isMatched = job.matchDetails.strongMatches.contains(req)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isMatched) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isMatched) SuccessGreen else GapOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = req,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isMatched) Slate900 else Slate500
                                    )
                                }
                            }
                        }
                    }
                }

                // Role Overview
                item {
                    SectionHeader(title = "About The Opportunity")
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = job.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Apply Action Button
                item {
                    JobPilotButton(
                        text = "Prepare Application with Profile",
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }
}
