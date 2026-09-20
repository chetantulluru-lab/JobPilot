package com.jobpilot.app.ui.screens.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
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
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    jobId: String,
    viewModel: JobViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSkillGap: (String) -> Unit,
    onNavigateToInterview: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(jobId) {
        viewModel.selectJobById(jobId)
    }

    val job = uiState.selectedJob

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(job?.title ?: "Job Details", style = MaterialTheme.typography.titleLarge) },
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
        if (job == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingState(message = "Retrieving job parameters...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Details
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${job.company} • ${job.location} • ${job.workMode}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate500
                        )
                        Text(
                            text = "${job.employmentType} • ${job.stipendOrSalary}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange600,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Match Score Card with Ring
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = BgWhite
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "AI Match Compatibility",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Slate500
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (job.matchDetails.matchScore != null) {
                                    Text(
                                        text = "${job.matchDetails.matchScore}% Match",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = Slate900
                                    )
                                    Text(
                                        text = job.matchDetails.matchTier?.name?.replace("_", " ") ?: "EVALUATED",
                                        style = JobPilotTypography.labelLarge,
                                        color = Orange600
                                    )
                                } else {
                                    Text(
                                        text = "Match Unavailable",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Slate800,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Complete your Career Profile or upload your resume to calculate AI alignment.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate500
                                    )
                                }
                            }
                            MatchScoreRing(score = job.matchDetails.matchScore, size = 70.dp)
                        }
                    }
                }

                // AI "Why This Matches You" Card
                if (job.matchDetails.whyItMatchesExplanation.isNotBlank()) {
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Orange50.copy(alpha = 0.7f),
                            borderColor = Orange300.copy(alpha = 0.6f)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Why this matches you",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Orange700,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = job.matchDetails.whyItMatchesExplanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Slate800,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // Skills Breakdown
                item {
                    if (job.matchDetails.strongMatches.isNotEmpty()) {
                        SectionHeader(title = "Strong Matches (${job.matchDetails.strongMatches.size})")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.matchDetails.strongMatches.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.STRONG_MATCH)
                            }
                        }
                    } else {
                        SectionHeader(title = "Required Skills (${job.requirements.size})")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.requirements.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.NEUTRAL)
                            }
                        }
                    }
                }

                // Missing / Weak Skills Breakdown
                item {
                    SectionHeader(title = "Potential Skill Gaps (${job.matchDetails.missingSkills.size})")
                    if (job.matchDetails.missingSkills.isEmpty()) {
                        Text(text = "None! You possess all primary required skills.", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            job.matchDetails.missingSkills.forEach { skill ->
                                SkillChip(skillName = skill, variant = ChipVariant.MISSING_GAP)
                            }
                        }
                    }
                }

                // If match is moderate or developing, provide direct button to view Skill Gap learning plan!
                if (job.matchDetails.improvementPlan.isNotEmpty()) {
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = GapOrangeBg,
                            borderColor = GapOrange.copy(alpha = 0.3f),
                            onClick = { onNavigateToSkillGap(job.id) }
                        ) {
                            Column {
                                Text(
                                    text = "Skill Gap Improvement Plan Available",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GapOrange,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tap to see 4 actionable steps to boost your match from ${job.matchDetails.matchScore}% to 90%+.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate700
                                )
                            }
                        }
                    }
                }

                // Full Job Requirements Checklist
                item {
                    SectionHeader(title = "Job Requirements")
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            job.requirements.forEach { req ->
                                val isMatched = job.matchDetails.strongMatches.contains(req)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isMatched) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isMatched) SuccessGreen else GapOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = req,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isMatched) Slate900 else Slate500
                                    )
                                }
                            }
                        }
                    }
                }

                // Role Overview
                item {
                    SectionHeader(title = "About The Opportunity")
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Text(
                            text = job.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate700,
                            lineHeight = 22.sp
                        )
                    }
                }

                // AI Career Acceleration Tools
                item {
                    SectionHeader(title = "AI Career Acceleration Tools")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1-Click Resume Tailor Button
                        OutlinedButton(
                            onClick = { viewModel.tailorResumeForSelectedJob() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange600),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Orange400)
                        ) {
                            if (uiState.isTailoring) {
                                CircularProgressIndicator(color = Orange600, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Adapting Resume with AI...")
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ 1-Click Tailor Resume for this Job", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Cold Outreach & Cover Letter Generator
                        OutlinedButton(
                            onClick = { viewModel.generateOutreachForSelectedJob() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate800),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate300)
                        ) {
                            if (uiState.isGeneratingOutreach) {
                                CircularProgressIndicator(color = Slate800, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Drafting Recruiter Outreach...")
                            } else {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Slate600, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("✉️ AI Cold Outreach & Cover Letter", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Practice Mock Interview
                        Button(
                            onClick = { onNavigateToInterview(job.title) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("🎙️ Practice Mock Interview for this Job", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Apply Action Button
                item {
                    JobPilotButton(
                        text = "Prepare Application with Profile",
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }

    // 1-Click Resume Tailor Dialog
    val tailorResult = uiState.tailoredResumeResult
    if (tailorResult != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissTailorDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Orange500)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Tailored Resume Ready!", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Score Comparison Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Orange50)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Before", fontSize = 11.sp, color = Slate500)
                            Text("${tailorResult.matchScoreBefore}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Slate700)
                        }
                        Text("➔", fontSize = 18.sp, color = Orange500, fontWeight = FontWeight.Bold)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tailored Match", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            Text("${tailorResult.matchScoreAfter}%", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                        }
                    }

                    Text("Tailored Professional Summary:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate800)
                    Text(
                        text = tailorResult.tailoredSummary,
                        fontSize = 12.sp,
                        color = Slate700,
                        lineHeight = 18.sp
                    )

                    if (tailorResult.matchedSkills.isNotEmpty()) {
                        Text("Targeted Keywords Inserted:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate800)
                        Text(
                            tailorResult.matchedSkills.joinToString(", "),
                            fontSize = 11.sp,
                            color = Orange600,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissTailorDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Done")
                }
            }
        )
    }

    // AI Outreach & Cover Letter Dialog
    val outreach = uiState.outreachContent
    if (outreach != null) {
        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("LinkedIn Note", "Cold Email", "Cover Letter")

        AlertDialog(
            onDismissRequest = { viewModel.dismissOutreachDialog() },
            title = {
                Text("Recruiter Outreach Suite", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TabRow(selectedTabIndex = selectedTab) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                            )
                        }
                    }

                    when (selectedTab) {
                        0 -> {
                            // LinkedIn Note (<300 chars)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Connection Note (${outreach.linkedinNote.length}/300 chars):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("LinkedIn Note", outreach.linkedinNote))
                                            android.widget.Toast.makeText(context, "Copied LinkedIn note!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Orange500, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Slate100)
                                        .padding(10.dp)
                                ) {
                                    Text(outreach.linkedinNote, fontSize = 12.sp, color = Slate800, lineHeight = 18.sp)
                                }
                            }
                        }
                        1 -> {
                            // Cold Email
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Cold Email Subject & Body:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                    IconButton(
                                        onClick = {
                                            val fullEmail = "Subject: ${outreach.coldEmailSubject}\n\n${outreach.coldEmailBody}"
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Cold Email", fullEmail))
                                            android.widget.Toast.makeText(context, "Copied cold email!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Orange500, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Slate100)
                                        .padding(10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("Subject: ${outreach.coldEmailSubject}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                        Divider(color = Slate300)
                                        Text(outreach.coldEmailBody, fontSize = 12.sp, color = Slate800, lineHeight = 18.sp)
                                    }
                                }
                            }
                        }
                        2 -> {
                            // Cover Letter
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Formal Cover Letter:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Cover Letter", outreach.coverLetter))
                                            android.widget.Toast.makeText(context, "Copied cover letter!", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Orange500, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Slate100)
                                        .padding(10.dp)
                                ) {
                                    Text(outreach.coverLetter, fontSize = 12.sp, color = Slate800, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissOutreachDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Done")
                }
            }
        )
    }
}
