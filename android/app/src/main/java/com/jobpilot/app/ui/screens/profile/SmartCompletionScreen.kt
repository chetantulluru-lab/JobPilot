package com.jobpilot.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCompletionScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToConnectedAccounts: () -> Unit
) {
    val detectedItems = listOf(
        Pair("Candidate Name", true),
        Pair("Education (Degree & CGPA)", true),
        Pair("Core Programming Skills", true),
        Pair("Academic & Side Projects", true),
        Pair("Internship Experience", true),
        Pair("Verified GitHub URL", false),
        Pair("LinkedIn Profile URL", false),
        Pair("Specific Project Timeline Dates", false),
        Pair("Personal Portfolio Website", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Profile Completion", style = MaterialTheme.typography.titleLarge) },
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AIOrb(size = 64.dp, showRings = false)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Your profile is almost ready",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Slate900
                        )
                        Text(
                            text = "Resume analysis detected 5 core categories. Complete missing fields below to maximize job matching.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                    }
                }
            }

            // Detected Checklist Summary Card
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Resume Extraction Audit",
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        detectedItems.forEach { (name, isDetected) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDetected) Slate800 else Slate500
                                )
                                if (isDetected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Detected",
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.HighlightOff,
                                        contentDescription = "Missing",
                                        tint = GapOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Action Items for Missing Data
            item {
                Text(
                    text = "Action Required: Missing Information",
                    style = MaterialTheme.typography.titleLarge,
                    color = Slate900
                )
            }

            // 1. GitHub
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "GitHub Profile", style = MaterialTheme.typography.titleMedium, color = Slate900)
                        Text(text = "Connect GitHub to verify public repository contributions.", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onNavigateToConnectedAccounts,
                                colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                                shape = ButtonShape,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Connect GitHub", color = Color.White, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = {},
                                shape = ButtonShape,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Add Manually", color = Slate700, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 2. LinkedIn
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "LinkedIn Profile", style = MaterialTheme.typography.titleMedium, color = Slate900)
                        Text(text = "Recruiters use LinkedIn to verify student status and activities.", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onNavigateToConnectedAccounts,
                                colors = ButtonDefaults.buttonColors(containerColor = InfoBlue),
                                shape = ButtonShape,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Connect LinkedIn", color = Color.White, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = {},
                                shape = ButtonShape,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text("Add Manually", color = Slate700, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 3. Project Dates
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Project Timeline Dates", style = MaterialTheme.typography.titleMedium, color = Slate900)
                        Text(text = "Your resume includes projects but lacks completion timelines.", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                            shape = ButtonShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Add Dates in Profile", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                JobPilotButton(
                    text = "Confirm & Return to Profile",
                    onClick = onNavigateBack
                )
            }
        }
    }
}
