package com.jobpilot.app.ui.screens.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
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
fun SkillGapDetailScreen(
    jobId: String,
    viewModel: JobViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(jobId) {
        viewModel.selectJobById(jobId)
    }

    val job = uiState.selectedJob

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skill Gap Diagnosis", style = MaterialTheme.typography.titleLarge) },
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
                LoadingState(message = "Loading diagnosis plan...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Slate900
                        )
                        Text(
                            text = "${job.company} • ${job.matchDetails.matchScore}% Match",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GapOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Why Match is Lower
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column {
                            Text(
                                text = "Why this role has a lower match score",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = job.matchDetails.whyItMatchesExplanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate700,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Comparison: You Match vs Missing Skills
                item {
                    SectionHeader(title = "Competency Breakdown")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // You Match
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            backgroundColor = SuccessGreenBg,
                            borderColor = SuccessGreen.copy(alpha = 0.3f)
                        ) {
                            Column {
                                Text(
                                    text = "You Match ✓",
                                    style = JobPilotTypography.labelLarge,
                                    color = SuccessGreen
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                job.matchDetails.strongMatches.forEach {
                                    Text(text = "• $it", style = MaterialTheme.typography.bodySmall, color = Slate800)
                                }
                            }
                        }

                        // Missing Skills
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            backgroundColor = GapOrangeBg,
                            borderColor = GapOrange.copy(alpha = 0.3f)
                        ) {
                            Column {
                                Text(
                                    text = "Missing Skills ✗",
                                    style = JobPilotTypography.labelLarge,
                                    color = GapOrange
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                job.matchDetails.missingSkills.forEach {
                                    Text(text = "• $it", style = MaterialTheme.typography.bodySmall, color = Slate800)
                                }
                            }
                        }
                    }
                }

                // "How To Improve Your Match" Actionable Learning Plan
                item {
                    SectionHeader(
                        title = "How To Improve Your Match",
                        subtitle = "Actionable roadmap to close skill gaps"
                    )
                }

                items(job.matchDetails.improvementPlan) { step ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Orange50,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${step.stepNumber}",
                                        color = Orange600,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = step.actionDescription,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate600,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = Orange500,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "~${step.estimatedDays} days • ${step.recommendedResource}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Orange600
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    JobPilotButton(
                        text = "Add Topics to Learning Goals",
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }
}
