package com.jobpilot.app.ui.screens.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.*
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ProfileViewModel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.jobpilot.app.data.network.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToSmartCompletion: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile


    var showAddSkillDialog by remember { mutableStateOf(false) }
    var showEditPersonalDialog by remember { mutableStateOf(false) }
    var showAddEducationDialog by remember { mutableStateOf(false) }
    var showAddProjectDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Career Profile", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                actions = {
                    IconButton(onClick = onNavigateToSmartCompletion) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Smart Completion",
                            tint = Orange500
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Profile Strength Banner
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Orange50.copy(alpha = 0.8f),
                    borderColor = Orange300.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Profile Strength: ${profile.profileStrengthScore}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = Orange700,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete missing links to reach 100%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600
                            )
                        }
                        TextButton(onClick = onNavigateToSmartCompletion) {
                            Text("Improve", color = Orange600, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 1. Personal Information Section
            item {
                SectionHeader(
                    title = "Personal Information",
                    actionText = "Edit",
                    onActionClick = { showEditPersonalDialog = true }
                )

                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val avatarUrl = profile.personalInfo.avatarUrl
                            val fullAvatarUrl = remember(avatarUrl) {
                                if (avatarUrl.isNullOrBlank()) null
                                else if (avatarUrl.startsWith("http")) avatarUrl
                                else ApiConfig.BASE_URL.removeSuffix("/api/v1/").removeSuffix("/") + avatarUrl
                            }

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Orange500),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = profile.personalInfo.fullName
                                    .split(" ")
                                    .filter { it.isNotBlank() }
                                    .take(2)
                                    .map { it.first().uppercase() }
                                    .joinToString("")
                                Text(
                                    text = initials.ifEmpty { "JP" },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.personalInfo.fullName.ifBlank { "Candidate" },
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                if (profile.personalInfo.age != null) {
                                    Text(
                                        text = "Age: ${profile.personalInfo.age}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.textSecondary
                                    )
                                }
                                Text(
                                    text = profile.personalInfo.email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.textMuted
                                )
                            }
                        }

                        if (!profile.personalInfo.college.isNullOrBlank() || !profile.personalInfo.degree.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Academics:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.textMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${profile.personalInfo.degree ?: ""} - ${profile.personalInfo.branch ?: ""}".trim().trim('-').trim(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.textPrimary
                            )
                            if (!profile.personalInfo.college.isNullOrBlank()) {
                                Text(
                                    text = profile.personalInfo.college ?: "",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.textSecondary
                                )
                            }
                        }

                        if (profile.personalInfo.phone.isNotBlank() || profile.personalInfo.location.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = listOf(profile.personalInfo.phone, profile.personalInfo.location).filter { it.isNotBlank() }.joinToString(" • "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.textMuted
                            )
                        }

                        if (profile.personalInfo.professionalSummary.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = profile.personalInfo.professionalSummary,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.textSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // 2. Education Section
            item {
                SectionHeader(
                    title = "Education",
                    actionText = "+ Add",
                    onActionClick = { showAddEducationDialog = true }
                )
            }

            items(profile.education) { edu ->
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = edu.degree, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.textPrimary, fontWeight = FontWeight.Bold)
                            Text(text = "${edu.college} • ${edu.branch}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textMuted)
                            Text(text = "${edu.startDate} - ${edu.endDate} • ${edu.grade}", style = MaterialTheme.typography.bodySmall, color = Orange500, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = { viewModel.removeEducation(edu.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                        }
                    }
                }
            }

            // 3. Technical Skills Section
            item {
                SectionHeader(
                    title = "Technical Skills",
                    actionText = "+ Add",
                    onActionClick = { showAddSkillDialog = true }
                )

                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            profile.skills.take(5).forEach { skill ->
                                SkillChip(skillName = skill.name, variant = ChipVariant.NEUTRAL)
                            }
                        }
                        if (profile.skills.size > 5) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                profile.skills.drop(5).forEach { skill ->
                                    SkillChip(skillName = skill.name, variant = ChipVariant.NEUTRAL)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Projects Section
            item {
                SectionHeader(
                    title = "Projects",
                    actionText = "+ Add",
                    onActionClick = { showAddProjectDialog = true }
                )
            }

            items(profile.projects) { proj ->
                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = proj.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.textPrimary, fontWeight = FontWeight.Bold)
                            Text(text = proj.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textSecondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Tech: ${proj.technologies.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = Orange500)
                            if (proj.githubUrl != null) {
                                Text(text = proj.githubUrl, style = MaterialTheme.typography.bodySmall, color = InfoBlue)
                            }
                        }
                        IconButton(onClick = { viewModel.removeProject(proj.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                        }
                    }
                }
            }

            // 5. Job Preferences Section (Render only if preferences exist, with stipend completely removed)
            if (profile.jobPreferences.targetRoles.isNotEmpty() || profile.jobPreferences.preferredLocations.isNotEmpty() || profile.jobPreferences.workMode.isNotBlank()) {
                item {
                    SectionHeader(title = "Job Preferences")

                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (profile.jobPreferences.targetRoles.isNotEmpty()) {
                                Text(text = "Target Roles: ${profile.jobPreferences.targetRoles.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.textSecondary)
                            }
                            if (profile.jobPreferences.preferredLocations.isNotEmpty()) {
                                Text(text = "Locations: ${profile.jobPreferences.preferredLocations.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.textSecondary)
                            }
                            if (profile.jobPreferences.workMode.isNotBlank()) {
                                Text(text = "Work Mode: ${profile.jobPreferences.workMode}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.textSecondary)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

    }

    // Add Skill Dialog
    if (showAddSkillDialog) {
        var skillName by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf(SkillCategory.PROGRAMMING_LANGUAGE) }

        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            title = { Text("Add New Skill") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = skillName,
                        onValueChange = { skillName = it },
                        label = { Text("Skill Name (e.g. Docker, PyTorch)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (skillName.isNotBlank()) {
                            viewModel.addSkill(skillName, selectedCategory)
                            showAddSkillDialog = false
                        }
                    }
                ) {
                    Text("Add", color = Orange500, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSkillDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Personal Info Dialog
    if (showEditPersonalDialog) {
        var name by remember { mutableStateOf(profile.personalInfo.fullName) }
        var ageText by remember { mutableStateOf(profile.personalInfo.age?.toString() ?: "") }
        var college by remember { mutableStateOf(profile.personalInfo.college ?: "") }
        var degree by remember { mutableStateOf(profile.personalInfo.degree ?: "") }
        var branch by remember { mutableStateOf(profile.personalInfo.branch ?: "") }
        var phone by remember { mutableStateOf(profile.personalInfo.phone) }
        var location by remember { mutableStateOf(profile.personalInfo.location) }
        var summary by remember { mutableStateOf(profile.personalInfo.professionalSummary) }

        AlertDialog(
            onDismissRequest = { showEditPersonalDialog = false },
            title = { Text("Edit Personal Information") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())
                ) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
                    OutlinedTextField(value = ageText, onValueChange = { ageText = it }, label = { Text("Age") })
                    OutlinedTextField(value = college, onValueChange = { college = it }, label = { Text("College / University") })
                    OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree (e.g. B.Tech)") })
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Branch (e.g. CSE)") })
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                    OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                    OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Summary") }, maxLines = 4)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updatePersonalInfo(
                            profile.personalInfo.copy(
                                fullName = name.trim(),
                                age = ageText.toIntOrNull(),
                                college = college.trim().ifEmpty { null },
                                degree = degree.trim().ifEmpty { null },
                                branch = branch.trim().ifEmpty { null },
                                phone = phone.trim(),
                                location = location.trim(),
                                professionalSummary = summary.trim()
                            )
                        )
                        showEditPersonalDialog = false
                    }
                ) {
                    Text("Save", color = Orange500, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditPersonalDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Education Dialog
    if (showAddEducationDialog) {
        var degree by remember { mutableStateOf("") }
        var college by remember { mutableStateOf("") }
        var branch by remember { mutableStateOf("") }
        var grade by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddEducationDialog = false },
            title = { Text("Add Education") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree (e.g. B.Tech)") })
                    OutlinedTextField(value = college, onValueChange = { college = it }, label = { Text("College / University") })
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Specialization / Branch") })
                    OutlinedTextField(value = grade, onValueChange = { grade = it }, label = { Text("CGPA / Grade") })
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (degree.isNotBlank() && college.isNotBlank()) {
                            viewModel.addEducation(
                                Education(
                                    id = "edu-${System.currentTimeMillis()}",
                                    degree = degree,
                                    college = college,
                                    branch = branch,
                                    startDate = "2023",
                                    endDate = "2027",
                                    grade = grade
                                )
                            )
                            showAddEducationDialog = false
                        }
                    }
                ) { Text("Add", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddEducationDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Project Dialog
    if (showAddProjectDialog) {
        var projectName by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var tech by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddProjectDialog = false },
            title = { Text("Add Project") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project Name") })
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
                    OutlinedTextField(value = tech, onValueChange = { tech = it }, label = { Text("Technologies (comma-separated)") })
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (projectName.isNotBlank()) {
                            viewModel.addProject(
                                Project(
                                    id = "proj-${System.currentTimeMillis()}",
                                    name = projectName,
                                    description = desc,
                                    technologies = tech.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                    startDate = "Jan 2026",
                                    endDate = "Present"
                                )
                            )
                            showAddProjectDialog = false
                        }
                    }
                ) { Text("Add", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddProjectDialog = false }) { Text("Cancel") }
            }
        )
    }
}
