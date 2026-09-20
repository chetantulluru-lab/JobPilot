package com.jobpilot.app.ui.screens.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.InterviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewSetupScreen(
    initialRole: String? = null,
    viewModel: InterviewViewModel,
    onNavigateBack: () -> Unit,
    onStartInterview: () -> Unit,
    onViewReport: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialRole) {
        if (!initialRole.isNullOrBlank()) {
            viewModel.setTargetRole(initialRole)
            viewModel.setMode("role")
        }
    }

    val experienceLevels = listOf("Entry-Level", "Mid-Level", "Senior / Lead")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AI Mock Interview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Realistic voice & video simulator", style = MaterialTheme.typography.bodySmall, color = Slate500)
                    }
                },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Intro Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Orange50.copy(alpha = 0.8f),
                    borderColor = Orange300.copy(alpha = 0.6f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AIOrb(size = 48.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Intelligent AI Interviewer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Orange700
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Questions are grounded in your actual projects, skills, or target role. Evaluated across 4 key hiring dimensions.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate700,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Mode Selector
            item {
                SectionHeader(title = "Interview Source")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Resume Mode Card
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setMode("resume") },
                        backgroundColor = if (uiState.mode == "resume") Orange50 else BgWhite,
                        borderColor = if (uiState.mode == "resume") Orange500 else Slate200
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = if (uiState.mode == "resume") Orange500 else Slate400,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "From My Resume",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (uiState.mode == "resume") Orange600 else Slate700
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tests your projects & listed skills",
                                fontSize = 11.sp,
                                color = Slate500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    // Target Role Mode Card
                    GlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setMode("role") },
                        backgroundColor = if (uiState.mode == "role") Orange50 else BgWhite,
                        borderColor = if (uiState.mode == "role") Orange500 else Slate200
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = if (uiState.mode == "role") Orange500 else Slate400,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Target Role",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (uiState.mode == "role") Orange600 else Slate700
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tests industry standards for role",
                                fontSize = 11.sp,
                                color = Slate500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // If Role mode or customizing role
            item {
                SectionHeader(title = if (uiState.mode == "resume") "Target Role Focus (Optional)" else "Target Role / Job Title")
                OutlinedTextField(
                    value = uiState.targetRole,
                    onValueChange = { viewModel.setTargetRole(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Android Engineer, Backend Developer, Data Scientist") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Orange500)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange500,
                        unfocusedBorderColor = Slate300
                    ),
                    singleLine = true
                )
            }

            // Experience Level Selector
            item {
                SectionHeader(title = "Experience Level")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    experienceLevels.forEach { level ->
                        val isSelected = uiState.experienceLevel == level
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Orange500 else BgWhite)
                                .border(1.dp, if (isSelected) Orange500 else Slate300, RoundedCornerShape(10.dp))
                                .clickable { viewModel.setExperienceLevel(level) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = level,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Slate700
                            )
                        }
                    }
                }
            }

            // What the Interview Covers checklist
            item {
                SectionHeader(title = "Interview Structure (5 Stages)")
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        StageRow(step = "1", title = "Candidate Introduction", desc = "Pitch yourself, background & current aspirations")
                        StageRow(step = "2", title = "Project Deep-Dive", desc = "Architecture, decisions & technical challenges")
                        StageRow(step = "3", title = "Technical Core", desc = "Algorithms, frameworks & system mechanics")
                        StageRow(step = "4", title = "Problem Solving & Scenarios", desc = "Debugging, scaling & tradeoff analysis")
                        StageRow(step = "5", title = "Behavioral & Collaboration (STAR)", desc = "Teamwork, delivery pressure & conflict resolution")
                    }
                }
            }

            // Start Interview Button
            item {
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                JobPilotButton(
                    text = if (uiState.isStarting) "Preparing AI Interview Room..." else "🎙️ Start AI Mock Interview",
                    enabled = !uiState.isStarting,
                    onClick = {
                        viewModel.startInterview(onSuccess = onStartInterview)
                    }
                )
            }

            // Past Interview History
            if (uiState.history.isNotEmpty()) {
                item {
                    SectionHeader(title = "Past Interview Sessions")
                }
                items(uiState.history) { session ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewReport(session.id) },
                        backgroundColor = BgWhite
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = session.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${session.targetRole} • ${session.experienceLevel}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            if (session.overallScore != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (session.overallScore >= 75) SuccessGreen.copy(alpha = 0.15f) else Orange50)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${session.overallScore}%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (session.overallScore >= 75) SuccessGreen else Orange600
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StageRow(step: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Orange500),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate800)
            Text(text = desc, fontSize = 11.sp, color = Slate500)
        }
    }
}
