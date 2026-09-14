package com.jobpilot.app.ui.screens.resume

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileSize by remember { mutableLongStateOf(0L) }
    var step by remember { mutableIntStateOf(1) } // 1 = Pick, 2 = Processing, 3 = Extracted Preview

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIdx = it.getColumnIndex(OpenableColumns.SIZE)
                    if (nameIdx != -1) selectedFileName = it.getString(nameIdx)
                    if (sizeIdx != -1) selectedFileSize = it.getLong(sizeIdx)
                }
            }
            if (selectedFileName == null) {
                selectedFileName = uri.lastPathSegment ?: "document.pdf"
            }
        }
    }

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    documentPickerLauncher.launch(
                                        arrayOf(
                                            "application/pdf",
                                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                            "application/msword"
                                        )
                                    )
                                },
                            backgroundColor = BgWhite,
                            contentPadding = 30.dp
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = if (selectedUri != null) Icons.Default.Description else Icons.Default.UploadFile,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = selectedFileName ?: "Tap to choose resume file",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Text(
                                    text = if (selectedUri != null) "${selectedFileSize / 1024} KB • Ready to parse" else "PDF, DOCX accepted",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (selectedUri != null) SuccessGreen else Slate500
                                )
                            }
                        }
                    }

                    JobPilotButton(
                        text = if (selectedUri != null) "Parse Resume with NLP" else "Choose File to Continue",
                        enabled = selectedUri != null,
                        onClick = {
                            val uri = selectedUri ?: return@JobPilotButton
                            step = 2
                            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                            if (bytes != null) {
                                val mimeType = context.contentResolver.getType(uri) ?: "application/pdf"
                                viewModel.uploadResumeFile(bytes, selectedFileName ?: "resume.pdf", mimeType) {
                                    step = 3
                                }
                            } else {
                                step = 1
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
                                    if (!parsed?.detectedName.isNullOrBlank()) {
                                        Text(text = "Candidate: ${parsed?.detectedName}", style = MaterialTheme.typography.titleMedium)
                                    }
                                    if (!parsed?.detectedEmail.isNullOrBlank()) {
                                        Text(text = "Email: ${parsed?.detectedEmail}", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                    }
                                    if (!parsed?.detectedPhone.isNullOrBlank()) {
                                        Text(text = "Phone: ${parsed?.detectedPhone}", style = MaterialTheme.typography.bodySmall, color = Slate600)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Skills Detected: ${parsed?.detectedSkills?.size ?: 0}", style = MaterialTheme.typography.labelMedium, color = Orange600)
                                    Text(text = "Experience Entries: ${parsed?.detectedExperience?.size ?: 0}", style = MaterialTheme.typography.labelMedium, color = Orange600)
                                    Text(text = "Projects Identified: ${parsed?.detectedProjects?.size ?: 0}", style = MaterialTheme.typography.labelMedium, color = Orange600)
                                }
                            }
                        }

                        if (!parsed?.detectedSkills.isNullOrEmpty()) {
                            item {
                                Text(
                                    text = "Extracted Competencies",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    parsed?.detectedSkills?.take(6)?.forEach { skill ->
                                        SkillChip(skillName = skill.name, variant = ChipVariant.STRONG_MATCH)
                                    }
                                }
                            }
                        }

                        if (!parsed?.missingFields.isNullOrEmpty()) {
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
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    JobPilotButton(
                        text = "Confirm & Save to Career Profile",
                        onClick = {
                            val rId = parsed?.resumeId
                            if (rId != null) {
                                viewModel.confirmResume(rId) {
                                    onNavigateToProfile()
                                }
                            } else {
                                onNavigateToProfile()
                            }
                        }
                    )
                }
            }
        }
    }
}
