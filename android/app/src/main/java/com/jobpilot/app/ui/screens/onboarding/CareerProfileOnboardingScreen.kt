package com.jobpilot.app.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.Education
import com.jobpilot.app.data.model.PersonalInfo
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ProfileViewModel

@Composable
fun CareerProfileOnboardingScreen(
    profileViewModel: ProfileViewModel,
    onFinishToHome: () -> Unit,
    onUploadResume: () -> Unit,
    onCreateResume: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()
    var currentStep by remember { mutableIntStateOf(1) } // 1: Personal, 2: Education, 3: Resume

    // Form inputs
    var fullName by remember { mutableStateOf(uiState.profile.personalInfo.fullName) }
    var ageText by remember { mutableStateOf(uiState.profile.personalInfo.age?.toString() ?: "") }
    var college by remember { mutableStateOf(uiState.profile.personalInfo.college ?: "") }
    var degree by remember { mutableStateOf(uiState.profile.personalInfo.degree ?: "") }
    var branch by remember { mutableStateOf(uiState.profile.personalInfo.branch ?: "") }

    fun saveProfileData() {
        val parsedAge = ageText.toIntOrNull()
        val updatedInfo = uiState.profile.personalInfo.copy(
            fullName = fullName.trim(),
            age = parsedAge,
            college = college.trim().ifEmpty { null },
            degree = degree.trim().ifEmpty { null },
            branch = branch.trim().ifEmpty { null }
        )
        profileViewModel.updatePersonalInfo(updatedInfo)

        if (college.isNotBlank() && degree.isNotBlank()) {
            profileViewModel.addEducation(
                Education(
                    id = "edu-${System.currentTimeMillis()}",
                    degree = degree.trim(),
                    college = college.trim(),
                    branch = branch.trim(),
                    startDate = "",
                    endDate = "",
                    grade = ""
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Navigation Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentStep > 1) {
                IconButton(onClick = { currentStep-- }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate700
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            Text(
                text = "Step $currentStep of 3",
                style = MaterialTheme.typography.labelLarge,
                color = Orange500,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Skip",
                style = MaterialTheme.typography.titleMedium,
                color = Slate500,
                modifier = Modifier
                    .clickable {
                        saveProfileData()
                        onFinishToHome()
                    }
                    .padding(8.dp)
            )
        }

        // Center Step Content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "CareerOnboardingSteps"
        ) { step ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AIOrb(size = 72.dp)
                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    1 -> {
                        Text(
                            text = "Tell Us About Yourself",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Slate900,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Set up your candidate identity for personalized roadmaps",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = fullName,
                                    onValueChange = { fullName = it },
                                    label = { Text("Full Name *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.medium,
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = ageText,
                                    onValueChange = { if (it.length <= 3) ageText = it.filter { char -> char.isDigit() } },
                                    label = { Text("Age") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.medium,
                                    singleLine = true
                                )
                            }
                        }
                    }

                    2 -> {
                        Text(
                            text = "Academic Background",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Slate900,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Helps AI customize career roadmaps to your degree level",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = college,
                                    onValueChange = { college = it },
                                    label = { Text("College / University *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.medium,
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = degree,
                                    onValueChange = { degree = it },
                                    label = { Text("Degree (e.g., B.Tech, MCA, B.S.)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.medium,
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = branch,
                                    onValueChange = { branch = it },
                                    label = { Text("Branch / Major (e.g., Computer Science)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.medium,
                                    singleLine = true
                                )
                            }
                        }
                    }

                    3 -> {
                        Text(
                            text = "Optional: Add Your Resume",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Slate900,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Upload an existing resume or create a fresh one anytime",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    saveProfileData()
                                    onUploadResume()
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.UploadFile,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Upload Existing Resume",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = Slate900
                                    )
                                    Text(
                                        text = "PDF or DOCX with instant NLP extraction",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    saveProfileData()
                                    onCreateResume()
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Create Resume with AI",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = Slate900
                                    )
                                    Text(
                                        text = "ATS-friendly templates crafted in minutes",
                                        fontSize = 12.sp,
                                        color = Slate500
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Actions & Progress Dots
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                repeat(3) { index ->
                    val isActive = index + 1 == currentStep
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(6.dp)
                            .width(if (isActive) 24.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isActive) Orange500 else Slate200)
                    )
                }
            }

            if (currentStep < 3) {
                JobPilotButton(
                    text = "Next",
                    onClick = {
                        if (currentStep == 1 && fullName.isNotBlank()) {
                            currentStep = 2
                        } else if (currentStep == 2) {
                            currentStep = 3
                        }
                    },
                    enabled = (currentStep != 1 || fullName.isNotBlank())
                )
            } else {
                JobPilotButton(
                    text = "Finish & Explore Roadmaps",
                    onClick = {
                        saveProfileData()
                        onFinishToHome()
                    }
                )
            }
        }
    }
}
