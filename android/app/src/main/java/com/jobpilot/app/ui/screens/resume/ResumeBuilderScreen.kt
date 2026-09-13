package com.jobpilot.app.ui.screens.resume

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.ResumeTemplateType
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ResumeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeBuilderScreen(
    viewModel: ResumeViewModel,
    onNavigateBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var resumeTitle by remember { mutableStateOf("Software Engineer - 2026") }
    var targetRoleToTailor by remember { mutableStateOf("Python Developer Intern") }
    var showTailorDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Resume Builder", style = MaterialTheme.typography.titleLarge) },
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
            // 1. Resume Name Field
            item {
                OutlinedTextField(
                    value = resumeTitle,
                    onValueChange = { resumeTitle = it },
                    label = { Text("Resume Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium
                )
            }

            // 2. 4 Distinct Template Cards with Visual Previews
            item {
                SectionHeader(
                    title = "Choose Template Design",
                    subtitle = "4 distinct architectural styles"
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResumeTemplateType.values().forEach { template ->
                        val isSelected = uiState.selectedTemplate == template

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectTemplate(template) },
                            shape = JobPilotShapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Orange50 else BgWhite
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Orange500 else Slate200
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Stylized mini resume layout preview
                                Box(
                                    modifier = Modifier
                                        .size(60.dp, 80.dp)
                                        .background(Color(android.graphics.Color.parseColor(template.accentColorHex)).copy(alpha = 0.15f), shape = JobPilotShapes.small)
                                        .padding(6.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(android.graphics.Color.parseColor(template.accentColorHex))))
                                        Box(modifier = Modifier.fillMaxWidth(0.6f).height(4.dp).background(Slate400))
                                        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Slate200))
                                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(4.dp).background(Slate200))
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = template.templateName,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Slate900
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = Orange500,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = template.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate600
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Best for: ${template.recommendedFor}",
                                        style = JobPilotTypography.labelMedium,
                                        color = Orange600
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Tailor Resume for Job Feature
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Tailor for Target Job",
                                style = MaterialTheme.typography.titleMedium,
                                color = Slate900
                            )
                            Text(
                                text = "Target: $targetRoleToTailor",
                                style = MaterialTheme.typography.bodySmall,
                                color = Orange600
                            )
                        }
                        OutlinedButton(
                            onClick = { showTailorDialog = true },
                            shape = ButtonShape
                        ) {
                            Text("Change Role")
                        }
                    }
                }
            }

            // 4. Action Buttons (AI Generate, Preview, Export PDF)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    JobPilotButton(
                        text = if (uiState.isGenerating) "AI Generating Resume..." else "Generate & Preview Resume",
                        onClick = {
                            viewModel.generateResume(resumeTitle) {
                                // Resume created
                            }
                        },
                        enabled = !uiState.isGenerating
                    )

                    JobPilotOutlinedButton(
                        text = "Export as Clean PDF Document",
                        onClick = {
                            val cleanName = resumeTitle.replace(Regex("[^a-zA-Z0-9]"), "_")
                            val pdfFile = java.io.File(context.cacheDir, "$cleanName.pdf")
                            val targetId = uiState.currentBuildingResume?.id ?: "current"
                            viewModel.exportPdf(targetId, pdfFile)
                        }
                    )
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

    if (showTailorDialog) {
        AlertDialog(
            onDismissRequest = { showTailorDialog = false },
            title = { Text("Tailor Resume For Job") },
            text = {
                Column {
                    Text(text = "Specify target job title. JobPilot will re-rank matching keywords from your profile:", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = targetRoleToTailor,
                        onValueChange = { targetRoleToTailor = it },
                        label = { Text("Target Role Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showTailorDialog = false
                    val targetId = uiState.currentBuildingResume?.id ?: "current"
                    viewModel.tailorResume(targetId, targetRoleToTailor) {}
                }) {
                    Text("Apply Tailoring", color = Orange500, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTailorDialog = false }) { Text("Cancel") }
            }
        )
    }

}
