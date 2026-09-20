package com.jobpilot.app.ui.screens.interview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.QuestionEvaluation
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.InterviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewReportScreen(
    sessionId: String,
    viewModel: InterviewViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRoadmaps: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        if (uiState.report == null || uiState.report?.sessionId != sessionId) {
            viewModel.loadReport(sessionId)
        }
    }

    val report = uiState.report

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Interview Evaluation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        bottomBar = {
            Surface(
                color = BgWhite,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToRoadmaps,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Explore Roadmaps", color = Slate700)
                    }
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Back to Home")
                    }
                }
            }
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        if (uiState.isLoadingReport) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingState(message = "Analyzing interview transcripts & video metrics...")
            }
        } else if (report == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Report not found or still generating.", color = Slate500)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadReport(sessionId) }) {
                        Text("Retry Loading")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Score Card
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = BgWhite
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = report.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Target: ${report.targetRole}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Score Circle
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (report.overallScore >= 75) SuccessGreen.copy(alpha = 0.12f) else Orange50
                                    )
                                    .border(
                                        3.dp,
                                        if (report.overallScore >= 75) SuccessGreen else Orange500,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${report.overallScore}",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (report.overallScore >= 75) SuccessGreen else Orange600
                                    )
                                    Text(
                                        text = "OUT OF 100",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate500
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (report.overallScore >= 75) SuccessGreen else Orange500)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = report.readinessBadge,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 4-Dimensional Metrics Breakdown
                item {
                    SectionHeader(title = "Core Dimension Performance")
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            MetricScoreRow(title = "Technical Depth & Accuracy", score = report.technicalScore, color = Orange500)
                            MetricScoreRow(title = "Communication & Clarity", score = report.communicationScore, color = Color(0xFF3B82F6))
                            MetricScoreRow(title = "Problem Solving & STAR Format", score = report.problemSolvingScore, color = Color(0xFF8B5CF6))
                            MetricScoreRow(title = "Video Presence & Eye Contact", score = report.presenceScore, color = SuccessGreen)
                        }
                    }
                }

                // AI Executive Summary
                item {
                    SectionHeader(title = "AI Executive Feedback")
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Orange50.copy(alpha = 0.5f),
                        borderColor = Orange300.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = report.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate800,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Strengths & Weaknesses
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Key Strengths
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            backgroundColor = BgWhite
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Key Strengths", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                                }
                                report.keyStrengths.forEach { s ->
                                    Text("• $s", fontSize = 11.sp, color = Slate700, lineHeight = 16.sp)
                                }
                            }
                        }

                        // Areas for Improvement
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            backgroundColor = BgWhite
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Orange500, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Growth Areas", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                                }
                                report.areasForImprovement.forEach { a ->
                                    Text("• $a", fontSize = 11.sp, color = Slate700, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                }

                // Recommended Topics
                if (report.recommendedRoadmapTopics.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Recommended Study Focus")
                        GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Bridge your interview gaps by exploring these concepts in your roadmap:",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    report.recommendedRoadmapTopics.forEach { topic ->
                                        SkillChip(skillName = topic, variant = ChipVariant.NEUTRAL)
                                    }
                                }
                            }
                        }
                    }
                }

                // Question By Question Breakdown with Model Answers
                item {
                    SectionHeader(title = "Question Breakdown & Model Answers (${report.questionEvaluations.size})")
                }

                items(report.questionEvaluations) { evaluation ->
                    QuestionEvaluationCard(evaluation = evaluation)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun MetricScoreRow(title: String, score: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Slate800)
            Text(text = "$score%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Slate100
        )
    }
}

@Composable
private fun QuestionEvaluationCard(evaluation: QuestionEvaluation) {
    var expanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = BgWhite
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Orange50)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = evaluation.category.uppercase(),
                            color = Orange600,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = evaluation.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (evaluation.score >= 70) SuccessGreen.copy(alpha = 0.12f) else Orange50)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${evaluation.score}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (evaluation.score >= 70) SuccessGreen else Orange600
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Divider(color = Slate200)

                    // Candidate Answer
                    Column {
                        Text("Your Answer:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                        Text(
                            text = if (evaluation.candidateAnswer.isBlank()) "(No response provided)" else evaluation.candidateAnswer,
                            fontSize = 12.sp,
                            color = Slate800,
                            fontStyle = if (evaluation.candidateAnswer.isBlank()) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal
                        )
                    }

                    // Evaluator Feedback
                    Column {
                        Text("Interviewer Feedback:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                        Text(
                            text = evaluation.feedback,
                            fontSize = 12.sp,
                            color = Slate700
                        )
                    }

                    // Ideal Model Answer
                    if (evaluation.modelAnswer.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Orange50.copy(alpha = 0.7f))
                                .border(1.dp, Orange200, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Orange500, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ideal Model Answer (Best Practice):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Orange700)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = evaluation.modelAnswer,
                                    fontSize = 12.sp,
                                    color = Slate800,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
