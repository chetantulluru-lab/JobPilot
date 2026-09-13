package com.jobpilot.app.ui.screens.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeHubScreen(
    viewModel: ResumeViewModel,
    onNavigateToUpload: () -> Unit,
    onNavigateToBuilder: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resume Hub", style = MaterialTheme.typography.headlineMedium) },
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
            // Two Main Flow Cards
            item {
                Text(
                    text = "Resume Actions",
                    style = MaterialTheme.typography.titleLarge,
                    color = Slate900
                )
            }

            // Option 1: Upload Existing Resume
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite,
                    onClick = onNavigateToUpload
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = JobPilotShapes.medium,
                            color = Orange50,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, tint = Orange500)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Upload Existing Resume",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate900
                            )
                            Text(
                                text = "Upload PDF or DOCX. JobPilot NLP extracts skills and coursework to seed your profile.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate400)
                    }
                }
            }

            // Option 2: Build Resume With JobPilot
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Orange50.copy(alpha = 0.6f),
                    borderColor = Orange300.copy(alpha = 0.5f),
                    onClick = onNavigateToBuilder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = JobPilotShapes.medium,
                            color = Orange500,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Build Resume With AI",
                                style = MaterialTheme.typography.titleMedium,
                                color = Orange700
                            )
                            Text(
                                text = "Generate tailored resumes from your Career Profile with 4 executive templates.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate700
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Orange500)
                    }
                }
            }

            // Generated / Active Resumes Section
            item {
                SectionHeader(
                    title = "Your Resumes (${uiState.resumes.size})",
                    subtitle = "Templates ready to download or tailor"
                )
            }

            items(uiState.resumes) { resume ->
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = resume.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                if (resume.isDefault) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(shape = ChipShape, color = Orange50) {
                                        Text(
                                            text = "Primary",
                                            color = Orange600,
                                            style = JobPilotTypography.labelMedium,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Template: ${resume.templateType.templateName} • Modified ${resume.lastModified}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }

                        IconButton(onClick = { viewModel.exportPdf(resume.id) }) {
                            Icon(Icons.Default.Download, contentDescription = "Export PDF", tint = Orange500)
                        }
                    }
                }
            }

            if (uiState.exportPdfSuccessMessage != null) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SuccessGreenBg) {
                        Text(
                            text = uiState.exportPdfSuccessMessage ?: "",
                            color = SuccessGreen,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
