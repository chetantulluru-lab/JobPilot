package com.jobpilot.app.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.network.ApiConfig
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToSmartCompletion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile
    val snackbarHostState = remember { SnackbarHostState() }

    // Always fetch latest profile data on screen enter
    LaunchedEffect(Unit) {
        viewModel.refreshProfile()
    }

    // Display feedback message whenever state updates
    LaunchedEffect(uiState.feedbackMessage) {
        uiState.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    // Dialog control states
    var showEditPersonalDialog by remember { mutableStateOf(false) }

    var showAddSkillDialog by remember { mutableStateOf(false) }

    var showAddEducationDialog by remember { mutableStateOf(false) }
    var editingEducation by remember { mutableStateOf<Education?>(null) }

    var showAddExperienceDialog by remember { mutableStateOf(false) }
    var editingExperience by remember { mutableStateOf<Experience?>(null) }

    var showAddProjectDialog by remember { mutableStateOf(false) }
    var editingProject by remember { mutableStateOf<Project?>(null) }

    var showAddCertificationDialog by remember { mutableStateOf(false) }
    var editingCertification by remember { mutableStateOf<Certification?>(null) }

    var showEditPreferencesDialog by remember { mutableStateOf(false) }

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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

            // 2. Technical Skills Section
            item {
                SectionHeader(
                    title = "Technical Skills",
                    actionText = "+ Add",
                    onActionClick = { showAddSkillDialog = true }
                )

                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    if (profile.skills.isEmpty()) {
                        Text(
                            text = "No technical skills added yet. Tap '+ Add' to include your skills.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            profile.skills.forEach { skill ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Slate100,
                                    border = BorderStroke(1.dp, Slate200)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                                    ) {
                                        Text(
                                            text = skill.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.textPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        IconButton(
                                            onClick = { viewModel.removeSkill(skill.id) },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove skill",
                                                tint = Slate500,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Education Section
            item {
                SectionHeader(
                    title = "Education",
                    actionText = "+ Add",
                    onActionClick = { showAddEducationDialog = true }
                )
            }

            if (profile.education.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = "No education history added yet. Tap '+ Add' to include your degrees.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                    }
                }
            } else {
                items(profile.education, key = { it.id }) { edu ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = edu.degree,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                val sub = listOf(edu.college, edu.branch).filter { it.isNotBlank() }.joinToString(" • ")
                                if (sub.isNotBlank()) {
                                    Text(text = sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textMuted)
                                }
                                val datesAndGrade = listOf(
                                    if (edu.startDate.isNotBlank() || edu.endDate.isNotBlank()) "${edu.startDate} - ${edu.endDate}".trim('-', ' ') else "",
                                    if (edu.grade.isNotBlank()) edu.grade else ""
                                ).filter { it.isNotBlank() }.joinToString(" • ")
                                if (datesAndGrade.isNotBlank()) {
                                    Text(text = datesAndGrade, style = MaterialTheme.typography.bodySmall, color = Orange500, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Row {
                                IconButton(onClick = { editingEducation = edu }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Slate600)
                                }
                                IconButton(onClick = { viewModel.removeEducation(edu.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Work Experience Section
            item {
                SectionHeader(
                    title = "Work Experience",
                    actionText = "+ Add",
                    onActionClick = { showAddExperienceDialog = true }
                )
            }

            if (profile.experience.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = "No work experience added yet. Tap '+ Add' to add internships or jobs.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                    }
                }
            } else {
                items(profile.experience, key = { it.id }) { exp ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = exp.role,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                val sub = listOf(exp.company, if (exp.startDate.isNotBlank() || exp.endDate.isNotBlank()) "${exp.startDate} - ${exp.endDate}".trim('-', ' ') else "").filter { it.isNotBlank() }.joinToString(" • ")
                                if (sub.isNotBlank()) {
                                    Text(text = sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textMuted)
                                }
                                if (exp.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = exp.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textSecondary)
                                }
                            }
                            Row {
                                IconButton(onClick = { editingExperience = exp }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Slate600)
                                }
                                IconButton(onClick = { viewModel.removeExperience(exp.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                                }
                            }
                        }
                    }
                }
            }

            // 5. Projects Section
            item {
                SectionHeader(
                    title = "Projects",
                    actionText = "+ Add",
                    onActionClick = { showAddProjectDialog = true }
                )
            }

            if (profile.projects.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = "No projects added yet. Tap '+ Add' to showcase your projects.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                    }
                }
            } else {
                items(profile.projects, key = { it.id }) { proj ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = proj.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                if (proj.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = proj.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textSecondary)
                                }
                                if (proj.technologies.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Tech: ${proj.technologies.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = Orange500)
                                }
                                if (!proj.githubUrl.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = proj.githubUrl, style = MaterialTheme.typography.bodySmall, color = InfoBlue)
                                }
                                if (!proj.liveUrl.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = proj.liveUrl, style = MaterialTheme.typography.bodySmall, color = InfoBlue)
                                }
                            }
                            Row {
                                IconButton(onClick = { editingProject = proj }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Slate600)
                                }
                                IconButton(onClick = { viewModel.removeProject(proj.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                                }
                            }
                        }
                    }
                }
            }

            // 6. Certifications Section
            item {
                SectionHeader(
                    title = "Certifications",
                    actionText = "+ Add",
                    onActionClick = { showAddCertificationDialog = true }
                )
            }

            if (profile.certifications.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = "No certifications added yet. Tap '+ Add' to showcase your credentials.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                    }
                }
            } else {
                items(profile.certifications, key = { it.id }) { cert ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cert.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.textPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                val sub = listOf(cert.issuingOrganization, cert.date).filter { it.isNotBlank() }.joinToString(" • ")
                                if (sub.isNotBlank()) {
                                    Text(text = sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.textMuted)
                                }
                                if (!cert.credentialUrl.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = cert.credentialUrl, style = MaterialTheme.typography.bodySmall, color = InfoBlue)
                                }
                            }
                            Row {
                                IconButton(onClick = { editingCertification = cert }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Slate600)
                                }
                                IconButton(onClick = { viewModel.removeCertification(cert.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate400)
                                }
                            }
                        }
                    }
                }
            }

            // 7. Job Preferences Section
            item {
                SectionHeader(
                    title = "Job Preferences",
                    actionText = "Edit",
                    onActionClick = { showEditPreferencesDialog = true }
                )

                GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Target Roles: ${profile.jobPreferences.targetRoles.joinToString(", ").ifBlank { "Not specified" }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.textSecondary
                        )
                        Text(
                            text = "Locations: ${profile.jobPreferences.preferredLocations.joinToString(", ").ifBlank { "Not specified" }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.textSecondary
                        )
                        val mode = (profile.jobPreferences.workMode ?: "Remote").ifBlank { "Remote" }
                        Text(
                            text = "Work Mode: $mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.textSecondary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

    }

    // ==========================================
    // DIALOGS
    // ==========================================

    // 1. Edit Personal Info Dialog
    if (showEditPersonalDialog) {
        var name by remember { mutableStateOf(profile.personalInfo.fullName) }
        var ageText by remember { mutableStateOf(profile.personalInfo.age?.toString() ?: "") }
        var college by remember { mutableStateOf(profile.personalInfo.college ?: "") }
        var degree by remember { mutableStateOf(profile.personalInfo.degree ?: "") }
        var branch by remember { mutableStateOf(profile.personalInfo.branch ?: "") }
        var phone by remember { mutableStateOf(profile.personalInfo.phone) }
        var location by remember { mutableStateOf(profile.personalInfo.location) }
        var summary by remember { mutableStateOf(profile.personalInfo.professionalSummary) }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showEditPersonalDialog = false },
            title = { Text("Edit Personal Information") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ageText, onValueChange = { ageText = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = college, onValueChange = { college = it }, label = { Text("College / University") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree (e.g. B.Tech)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Branch (e.g. CSE)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Summary") }, maxLines = 4, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.isBlank()) {
                            error = "Full Name is required"
                        } else {
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

    // 2. Add Skill Dialog
    if (showAddSkillDialog) {
        var skillName by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf(SkillCategory.PROGRAMMING_LANGUAGE) }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            title = { Text("Add Technical Skill") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(
                        value = skillName,
                        onValueChange = { skillName = it },
                        label = { Text("Skill Name (e.g. Kotlin, Docker, Python)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Category:", style = MaterialTheme.typography.labelMedium, color = Slate600)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SkillCategory.values().forEach { cat ->
                            val isSelected = selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (skillName.isBlank()) {
                            error = "Skill name cannot be empty"
                        } else {
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

    // 3. Add Education Dialog
    if (showAddEducationDialog) {
        var degree by remember { mutableStateOf("") }
        var college by remember { mutableStateOf("") }
        var branch by remember { mutableStateOf("") }
        var startYear by remember { mutableStateOf("") }
        var endYear by remember { mutableStateOf("") }
        var grade by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddEducationDialog = false },
            title = { Text("Add Education") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree * (e.g. B.Tech, M.S.)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = college, onValueChange = { college = it }, label = { Text("College / University *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Specialization / Branch") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = startYear, onValueChange = { startYear = it }, label = { Text("Start Year") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = endYear, onValueChange = { endYear = it }, label = { Text("End Year") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = grade, onValueChange = { grade = it }, label = { Text("CGPA / Percentage") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (degree.isBlank() || college.isBlank()) {
                            error = "Degree and College are required"
                        } else {
                            viewModel.addEducation(
                                Education(
                                    id = "edu-${System.currentTimeMillis()}",
                                    degree = degree.trim(),
                                    college = college.trim(),
                                    branch = branch.trim(),
                                    startDate = startYear.trim(),
                                    endDate = endYear.trim(),
                                    grade = grade.trim()
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

    // Edit Education Dialog
    editingEducation?.let { edu ->
        var degree by remember { mutableStateOf(edu.degree) }
        var college by remember { mutableStateOf(edu.college) }
        var branch by remember { mutableStateOf(edu.branch) }
        var startYear by remember { mutableStateOf(edu.startDate) }
        var endYear by remember { mutableStateOf(edu.endDate) }
        var grade by remember { mutableStateOf(edu.grade) }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { editingEducation = null },
            title = { Text("Edit Education") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = degree, onValueChange = { degree = it }, label = { Text("Degree *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = college, onValueChange = { college = it }, label = { Text("College / University *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Specialization / Branch") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = startYear, onValueChange = { startYear = it }, label = { Text("Start Year") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = endYear, onValueChange = { endYear = it }, label = { Text("End Year") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = grade, onValueChange = { grade = it }, label = { Text("CGPA / Percentage") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (degree.isBlank() || college.isBlank()) {
                            error = "Degree and College are required"
                        } else {
                            viewModel.updateEducation(
                                edu.copy(
                                    degree = degree.trim(),
                                    college = college.trim(),
                                    branch = branch.trim(),
                                    startDate = startYear.trim(),
                                    endDate = endYear.trim(),
                                    grade = grade.trim()
                                )
                            )
                            editingEducation = null
                        }
                    }
                ) { Text("Save", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editingEducation = null }) { Text("Cancel") }
            }
        )
    }

    // 4. Add Experience Dialog
    if (showAddExperienceDialog) {
        var role by remember { mutableStateOf("") }
        var company by remember { mutableStateOf("") }
        var startDate by remember { mutableStateOf("") }
        var endDate by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddExperienceDialog = false },
            title = { Text("Add Experience") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Job Title / Role * (e.g. Frontend Intern)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company * (e.g. Google)") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, maxLines = 4, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (role.isBlank() || company.isBlank()) {
                            error = "Role and Company are required"
                        } else {
                            viewModel.addExperience(
                                Experience(
                                    id = "exp-${System.currentTimeMillis()}",
                                    role = role.trim(),
                                    company = company.trim(),
                                    startDate = startDate.trim(),
                                    endDate = endDate.trim(),
                                    description = description.trim(),
                                    technologies = emptyList()
                                )
                            )
                            showAddExperienceDialog = false
                        }
                    }
                ) { Text("Add", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddExperienceDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Edit Experience Dialog
    editingExperience?.let { exp ->
        var role by remember { mutableStateOf(exp.role) }
        var company by remember { mutableStateOf(exp.company) }
        var startDate by remember { mutableStateOf(exp.startDate) }
        var endDate by remember { mutableStateOf(exp.endDate) }
        var description by remember { mutableStateOf(exp.description) }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { editingExperience = null },
            title = { Text("Edit Experience") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Job Title / Role *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company *") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, maxLines = 4, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (role.isBlank() || company.isBlank()) {
                            error = "Role and Company are required"
                        } else {
                            viewModel.updateExperience(
                                exp.copy(
                                    role = role.trim(),
                                    company = company.trim(),
                                    startDate = startDate.trim(),
                                    endDate = endDate.trim(),
                                    description = description.trim()
                                )
                            )
                            editingExperience = null
                        }
                    }
                ) { Text("Save", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editingExperience = null }) { Text("Cancel") }
            }
        )
    }

    // 5. Add Project Dialog
    if (showAddProjectDialog) {
        var projectName by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var tech by remember { mutableStateOf("") }
        var githubUrl by remember { mutableStateOf("") }
        var liveUrl by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddProjectDialog = false },
            title = { Text("Add Project") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project Name *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, maxLines = 3, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = tech, onValueChange = { tech = it }, label = { Text("Technologies (comma-separated)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = githubUrl, onValueChange = { githubUrl = it }, label = { Text("GitHub URL") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = liveUrl, onValueChange = { liveUrl = it }, label = { Text("Live Demo URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (projectName.isBlank()) {
                            error = "Project Name is required"
                        } else {
                            viewModel.addProject(
                                Project(
                                    id = "proj-${System.currentTimeMillis()}",
                                    name = projectName.trim(),
                                    description = desc.trim(),
                                    technologies = tech.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                    startDate = "",
                                    endDate = "",
                                    githubUrl = githubUrl.trim().ifEmpty { null },
                                    liveUrl = liveUrl.trim().ifEmpty { null }
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

    // Edit Project Dialog
    editingProject?.let { proj ->
        var projectName by remember { mutableStateOf(proj.name) }
        var desc by remember { mutableStateOf(proj.description) }
        var tech by remember { mutableStateOf(proj.technologies.joinToString(", ")) }
        var githubUrl by remember { mutableStateOf(proj.githubUrl ?: "") }
        var liveUrl by remember { mutableStateOf(proj.liveUrl ?: "") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { editingProject = null },
            title = { Text("Edit Project") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project Name *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, maxLines = 3, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = tech, onValueChange = { tech = it }, label = { Text("Technologies (comma-separated)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = githubUrl, onValueChange = { githubUrl = it }, label = { Text("GitHub URL") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = liveUrl, onValueChange = { liveUrl = it }, label = { Text("Live Demo URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (projectName.isBlank()) {
                            error = "Project Name is required"
                        } else {
                            viewModel.updateProject(
                                proj.copy(
                                    name = projectName.trim(),
                                    description = desc.trim(),
                                    technologies = tech.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                    githubUrl = githubUrl.trim().ifEmpty { null },
                                    liveUrl = liveUrl.trim().ifEmpty { null }
                                )
                            )
                            editingProject = null
                        }
                    }
                ) { Text("Save", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editingProject = null }) { Text("Cancel") }
            }
        )
    }

    // 6. Add Certification Dialog
    if (showAddCertificationDialog) {
        var certName by remember { mutableStateOf("") }
        var issuer by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var credentialUrl by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showAddCertificationDialog = false },
            title = { Text("Add Certification") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = certName, onValueChange = { certName = it }, label = { Text("Certification Name *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = issuer, onValueChange = { issuer = it }, label = { Text("Issuing Organization * (e.g. AWS, Coursera)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Issue Date (e.g. 2025)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = credentialUrl, onValueChange = { credentialUrl = it }, label = { Text("Credential URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (certName.isBlank() || issuer.isBlank()) {
                            error = "Certification Name and Issuer are required"
                        } else {
                            viewModel.addCertification(
                                Certification(
                                    id = "cert-${System.currentTimeMillis()}",
                                    name = certName.trim(),
                                    issuingOrganization = issuer.trim(),
                                    date = date.trim(),
                                    credentialUrl = credentialUrl.trim().ifEmpty { null }
                                )
                            )
                            showAddCertificationDialog = false
                        }
                    }
                ) { Text("Add", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddCertificationDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Edit Certification Dialog
    editingCertification?.let { cert ->
        var certName by remember { mutableStateOf(cert.name) }
        var issuer by remember { mutableStateOf(cert.issuingOrganization) }
        var date by remember { mutableStateOf(cert.date) }
        var credentialUrl by remember { mutableStateOf(cert.credentialUrl ?: "") }
        var error by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { editingCertification = null },
            title = { Text("Edit Certification") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (error != null) {
                        Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    OutlinedTextField(value = certName, onValueChange = { certName = it }, label = { Text("Certification Name *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = issuer, onValueChange = { issuer = it }, label = { Text("Issuing Organization *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Issue Date") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = credentialUrl, onValueChange = { credentialUrl = it }, label = { Text("Credential URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (certName.isBlank() || issuer.isBlank()) {
                            error = "Certification Name and Issuer are required"
                        } else {
                            viewModel.updateCertification(
                                cert.copy(
                                    name = certName.trim(),
                                    issuingOrganization = issuer.trim(),
                                    date = date.trim(),
                                    credentialUrl = credentialUrl.trim().ifEmpty { null }
                                )
                            )
                            editingCertification = null
                        }
                    }
                ) { Text("Save", color = Orange500, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editingCertification = null }) { Text("Cancel") }
            }
        )
    }

    // 7. Edit Job Preferences Dialog
    if (showEditPreferencesDialog) {
        var roles by remember { mutableStateOf(profile.jobPreferences.targetRoles.joinToString(", ")) }
        var locations by remember { mutableStateOf(profile.jobPreferences.preferredLocations.joinToString(", ")) }
        var selectedWorkMode by remember { mutableStateOf((profile.jobPreferences.workMode ?: "Remote").ifBlank { "Remote" }) }

        AlertDialog(
            onDismissRequest = { showEditPreferencesDialog = false },
            title = { Text("Edit Job Preferences") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = roles,
                        onValueChange = { roles = it },
                        label = { Text("Target Roles (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locations,
                        onValueChange = { locations = it },
                        label = { Text("Preferred Locations (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Work Mode:", style = MaterialTheme.typography.labelMedium, color = Slate600)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Remote", "Hybrid", "On-site").forEach { mode ->
                            FilterChip(
                                selected = selectedWorkMode.equals(mode, ignoreCase = true),
                                onClick = { selectedWorkMode = mode },
                                label = { Text(mode) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updatePreferences(
                            profile.jobPreferences.copy(
                                targetRoles = roles.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                preferredLocations = locations.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                workMode = selectedWorkMode
                            )
                        )
                        showEditPreferencesDialog = false
                    }
                ) {
                    Text("Save", color = Orange500, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditPreferencesDialog = false }) { Text("Cancel") }
            }
        )
    }
}
