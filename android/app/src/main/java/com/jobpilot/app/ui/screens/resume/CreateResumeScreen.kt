package com.jobpilot.app.ui.screens.resume

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.jobpilot.app.data.model.ResumeTemplateType
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ProfileViewModel
import com.jobpilot.app.ui.viewmodel.ResumeViewModel
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel
import java.io.File

data class ProjectItemInput(
    var title: String = "",
    var techStack: String = "",
    var description: String = "",
    var githubUrl: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateResumeScreen(
    resumeViewModel: ResumeViewModel,
    profileViewModel: ProfileViewModel,
    roadmapViewModel: RoadmapViewModel
) {
    val context = LocalContext.current
    val profileState by profileViewModel.uiState.collectAsState()
    val roadmapState by roadmapViewModel.uiState.collectAsState()
    val resumeUiState by resumeViewModel.uiState.collectAsState()

    val profile = profileState.profile

    // Personal & Academic fields initialized from user profile
    var fullName by remember(profile.personalInfo.fullName) { mutableStateOf(profile.personalInfo.fullName) }
    var email by remember(profile.personalInfo.email) { mutableStateOf(profile.personalInfo.email) }
    var phone by remember(profile.personalInfo.phone) { mutableStateOf(profile.personalInfo.phone ?: "") }
    var college by remember(profile.personalInfo.college) { mutableStateOf(profile.personalInfo.college ?: "") }
    var branch by remember(profile.personalInfo.degree) { mutableStateOf(profile.personalInfo.degree ?: "B.Tech Computer Science & Engineering") }
    var cgpa by remember { mutableStateOf("8.5 / 10") }
    var gradYear by remember { mutableStateOf("2026") }

    // Links
    var githubUrl by remember { mutableStateOf("https://github.com/") }
    var linkedinUrl by remember { mutableStateOf("https://linkedin.com/in/") }

    // Skills
    val initialSkills = remember(profile.skills) {
        profile.skills.map { it.name }.joinToString(", ").ifBlank { "Java, Python, Data Structures & Algorithms, Spring Boot, React, SQL, Git" }
    }
    var skillsText by remember { mutableStateOf(initialSkills) }

    // Projects list
    val projectsList = remember {
        mutableStateListOf(
            ProjectItemInput(
                title = "JobPilot Career & Learning Platform",
                techStack = "Kotlin, Jetpack Compose, FastAPI, PostgreSQL",
                description = "Built an intelligent mobile career platform featuring structured CSE roadmaps and instant ATS resume generation.",
                githubUrl = "https://github.com/project"
            ),
            ProjectItemInput(
                title = "Distributed High-Throughput Task Queue",
                techStack = "Python, Redis, Docker, Asyncio",
                description = "Architected an asynchronous worker queue handling 5,000 requests/sec with exponential backoff and persistent state.",
                githubUrl = "https://github.com/project"
            )
        )
    }

    // Optional Experience & Achievements
    var experienceText by remember { mutableStateOf("Software Engineering Intern — Contributed to microservices API development, automated unit tests with Pytest, and optimized database queries.") }
    var achievementsText by remember { mutableStateOf("Solved 300+ Data Structures & Algorithms problems on LeetCode • Smart India Hackathon Finalist") }

    var isExporting by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Resume", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle info
            Text(
                text = "ATS-Friendly Single Page Resume",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Recruiter-compliant format designed to pass Applicant Tracking Systems with maximum score.",
                fontSize = 12.sp,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Personal & Contact Information
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("1. Personal Details", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        singleLine = true
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.weight(1f),
                            shape = JobPilotShapes.small,
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.weight(1f),
                            shape = JobPilotShapes.small,
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Academic Identity
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("2. Education & College Details", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                    OutlinedTextField(
                        value = college,
                        onValueChange = { college = it },
                        label = { Text("College / University Name") },
                        placeholder = { Text("e.g. National Institute of Technology") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("Degree & Branch") },
                        placeholder = { Text("e.g. B.Tech Computer Science & Engineering") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        singleLine = true
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = cgpa,
                            onValueChange = { cgpa = it },
                            label = { Text("CGPA / Score") },
                            placeholder = { Text("8.8 / 10") },
                            modifier = Modifier.weight(1f),
                            shape = JobPilotShapes.small,
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = gradYear,
                            onValueChange = { gradYear = it },
                            label = { Text("Graduation Year") },
                            placeholder = { Text("2026") },
                            modifier = Modifier.weight(1f),
                            shape = JobPilotShapes.small,
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Profiles & Online Links
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("3. Links & Social Profiles", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                    OutlinedTextField(
                        value = githubUrl,
                        onValueChange = { githubUrl = it },
                        label = { Text("GitHub Profile Link") },
                        leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = Orange500) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = linkedinUrl,
                        onValueChange = { linkedinUrl = it },
                        label = { Text("LinkedIn Profile Link") },
                        leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = Orange500) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Technical Skills
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("4. Technical Skills", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                        // 1-Tap Import from Roadmaps
                        TextButton(
                            onClick = {
                                val roadmapSkills = roadmapState.roadmaps
                                    .flatMap { listOf(it.goal, it.title) }
                                    .distinct()
                                if (roadmapSkills.isNotEmpty()) {
                                    val currentList = skillsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    val combined = (currentList + roadmapSkills).distinct().joinToString(", ")
                                    skillsText = combined
                                    statusMessage = "Imported skills from your roadmaps!"
                                } else {
                                    statusMessage = "No completed roadmaps found to import from."
                                }
                            }
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp), tint = Orange500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Import Roadmap Skills", fontSize = 11.sp, color = Orange500)
                        }
                    }

                    OutlinedTextField(
                        value = skillsText,
                        onValueChange = { skillsText = it },
                        label = { Text("Comma-separated Skills") },
                        placeholder = { Text("Java, Python, DSA, FastAPI, React, SQL, Docker...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        minLines = 2,
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Projects
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("5. Key Projects (${projectsList.size})", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                        TextButton(
                            onClick = {
                                projectsList.add(ProjectItemInput(title = "New Project", techStack = "Tech Stack", description = "Project description and impact"))
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Orange500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Project", fontSize = 12.sp, color = Orange500)
                        }
                    }

                    projectsList.forEachIndexed { index, proj ->
                        Surface(
                            shape = JobPilotShapes.small,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Project ${index + 1}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                    if (projectsList.size > 1) {
                                        IconButton(onClick = { projectsList.removeAt(index) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = proj.title,
                                    onValueChange = {
                                        projectsList[index] = proj.copy(title = it)
                                    },
                                    label = { Text("Project Title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.small,
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = proj.techStack,
                                    onValueChange = {
                                        projectsList[index] = proj.copy(techStack = it)
                                    },
                                    label = { Text("Technologies Used") },
                                    placeholder = { Text("e.g. React, Node.js, PostgreSQL") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.small,
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = proj.description,
                                    onValueChange = {
                                        projectsList[index] = proj.copy(description = it)
                                    },
                                    label = { Text("Bullet Description / Impact") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.small,
                                    minLines = 2,
                                    maxLines = 4
                                )

                                OutlinedTextField(
                                    value = proj.githubUrl,
                                    onValueChange = {
                                        projectsList[index] = proj.copy(githubUrl = it)
                                    },
                                    label = { Text("GitHub / Live Link") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = JobPilotShapes.small,
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Practical Experience & Achievements
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("6. Experience & Achievements (Optional)", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)

                    OutlinedTextField(
                        value = experienceText,
                        onValueChange = { experienceText = it },
                        label = { Text("Internship / Experience Summary") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        minLines = 2,
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = achievementsText,
                        onValueChange = { achievementsText = it },
                        label = { Text("Key Achievements & Certifications") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.small,
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Status notification
            statusMessage?.let {
                Text(
                    text = it,
                    color = Orange600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Export & Download Button
            JobPilotButton(
                text = if (isExporting) "Generating ATS Resume PDF..." else "Download ATS Resume (PDF)",
                onClick = {
                    isExporting = true
                    statusMessage = null

                    // Generate file in Downloads / Documents
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        ?: context.filesDir
                    val safeName = fullName.replace(" ", "_").ifBlank { "JobPilot" }
                    val destPdfFile = File(downloadsDir, "${safeName}_ATS_Resume.pdf")

                    resumeViewModel.exportPdf("current", destPdfFile)

                    // Open / view PDF
                    try {
                        val contentUri: Uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            destPdfFile
                        )
                        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(contentUri, "application/pdf")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(viewIntent)
                        statusMessage = "Resume saved to Downloads: ${destPdfFile.name}"
                    } catch (e: Exception) {
                        statusMessage = "Resume generated successfully (${destPdfFile.name})!"
                    }
                    isExporting = false
                },
                enabled = !isExporting,
                leadingIcon = {
                    if (isExporting) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
