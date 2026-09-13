package com.jobpilot.app.ui.screens.resume

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeUploadFlowScreen(
    viewModel: ResumeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFile by remember { mutableStateOf<String?>("chetan_resume_2026.pdf") }
    var step by remember { mutableIntStateOf(1) } // 1 = Pick, 2 = Processing, 3 = Extracted Preview

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Resume (NLP Flow)", style = MaterialTheme.typography.titleLarge) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (step) {
                // Step 1: File Selection
                1 -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select Career Document",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Slate900
                        )
                        Text(
                            text = "Supports PDF and DOCX. JobPilot uses NLP algorithms to extract education, skills, and projects.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = BgWhite,
                            contentPadding = 30.dp
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.UploadFile,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = selectedFile ?: "Tap to choose resume file",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Text(
                                    text = "Ready to parse (Mock document selected)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    JobPilotButton(
                        text = "Parse Resume with NLP",
                        onClick = {
                            step = 2
                            viewModel.simulateUploadResume("chetan_resume_2026.pdf") {
                                step = 3
                            }
                        }
                    )
                }

                // Step 2: Processing UI
                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingState(message = "JobPilot NLP Parsing...\nTokenizing text, extracting entities & mapping taxonomy")
                    }
                }

                // Step 3: Extracted Preview & Confirmation
                3 -> {
                    val parsed = uiState.parsedResumeData
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Extracted Resume Information",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Slate900
                            )
                            Text(
                                text = "Review extracted details before updating your Career Profile.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }

                        item {
                            GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                                Column {
                                    Text(text = "Candidate Name: ${parsed?.detectedName}", style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Email: ${parsed?.detectedEmail}", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                    Text(text = "Phone: ${parsed?.detectedPhone}", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Skills Extracted: ${parsed?.detectedSkills?.size ?: 0} skills detected", style = MaterialTheme.typography.labelMedium, color = Orange600)
                                    Text(text = "Projects Extracted: ${parsed?.detectedProjects?.size ?: 0} projects identified", style = MaterialTheme.typography.labelMedium, color = Orange600)
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Missing Fields Detected (${parsed?.missingFields?.size ?: 0})",
                                style = MaterialTheme.typography.titleMedium,
                                color = GapOrange
                            )
                        }

                        items(parsed?.missingFields ?: emptyList()) { missing ->
                            GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = GapOrangeBg) {
                                Column {
                                    Text(text = missing.fieldLabel, style = MaterialTheme.typography.titleMedium, color = GapOrange, fontWeight = FontWeight.Bold)
                                    Text(text = missing.reason, style = MaterialTheme.typography.bodySmall, color = Slate700)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    JobPilotButton(
                        text = "Confirm & Save to Career Profile",
                        onClick = onNavigateToProfile
                    )
                }
            }
        }
    }
}
