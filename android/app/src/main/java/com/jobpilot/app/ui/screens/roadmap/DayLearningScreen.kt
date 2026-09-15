package com.jobpilot.app.ui.screens.roadmap

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.PracticeTask
import com.jobpilot.app.data.model.RoadmapDay
import com.jobpilot.app.data.model.RoadmapResource
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.InAppVideoPlayer
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@Composable
fun DayLearningScreen(
    roadmapId: String,
    dayId: String,
    viewModel: RoadmapViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Find day from loaded roadmap
    val currentRoadmap = uiState.currentRoadmap
    var activeDay by remember { mutableStateOf<RoadmapDay?>(null) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var userCustomVideoUrl by remember { mutableStateOf<String?>(null) }

    val allDays = remember(currentRoadmap) {
        currentRoadmap?.phases?.flatMap { it.days }?.sortedBy { it.dayNumber } ?: emptyList()
    }

    LaunchedEffect(currentRoadmap, dayId) {
        if (currentRoadmap != null) {
            for (phase in currentRoadmap.phases) {
                val found = phase.days.firstOrNull { it.id == dayId }
                if (found != null) {
                    activeDay = found
                    viewModel.loadPhaseResources(found.phaseId, uiState.selectedLanguage)
                    break
                }
            }
        } else {
            viewModel.loadRoadmapDetail(roadmapId)
        }
    }

    val day = activeDay

    if (day == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Orange500)
        }
        return
    }

    val currentDayIndex = allDays.indexOfFirst { it.id == day.id }
    val hasPreviousDay = currentDayIndex > 0
    val hasNextDay = currentDayIndex >= 0 && currentDayIndex < allDays.size - 1
    val previousDay = if (hasPreviousDay) allDays[currentDayIndex - 1] else null
    val nextDay = if (hasNextDay) allDays[currentDayIndex + 1] else null

    // Determine current video URL
    val currentVideoUrl = userCustomVideoUrl ?: uiState.phaseResources
        .firstOrNull { it.resourceType == "video" && (it.dayId == null || it.dayId == day.id) }
        ?.url

    val languages = listOf("English", "Telugu", "Hindi")
    val tabTitles = listOf("🎥 Video Lesson", "📖 Concepts", "💻 Practice Lab", "🤖 AI Tutor")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Top Bar ---
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Day ${day.dayNumber}: ${day.topic}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.textPrimary,
                            maxLines = 1
                        )
                        val phaseTitle = currentRoadmap?.phases?.firstOrNull { it.id == day.phaseId }?.title ?: "Curriculum"
                        Text(
                            text = "$phaseTitle • Day ${day.dayNumber} of ${allDays.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.textSecondary
                        )
                    }

                    if (day.isCompleted) {
                        Surface(
                            shape = JobPilotShapes.small,
                            color = SuccessGreenBg,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Completed",
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day-by-Day Stepper Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(JobPilotShapes.small)
                        .background(MaterialTheme.cardBg)
                        .border(1.dp, MaterialTheme.cardBorder, JobPilotShapes.small)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            if (previousDay != null) {
                                userCustomVideoUrl = null
                                activeDay = previousDay
                                viewModel.loadPhaseResources(previousDay.phaseId, uiState.selectedLanguage)
                            }
                        },
                        enabled = hasPreviousDay,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hasPreviousDay) "Day ${previousDay?.dayNumber}" else "Start",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Day ${day.dayNumber} / ${allDays.size}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Orange500
                    )

                    TextButton(
                        onClick = {
                            if (nextDay != null) {
                                userCustomVideoUrl = null
                                activeDay = nextDay
                                viewModel.loadPhaseResources(nextDay.phaseId, uiState.selectedLanguage)
                            }
                        },
                        enabled = hasNextDay,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (hasNextDay) "Day ${nextDay?.dayNumber}" else "Final",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // --- Clean Segmented Navigation Tabs ---
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Orange500,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Orange500,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTabIndex == index) Orange500 else MaterialTheme.textSecondary
                        )
                    }
                )
            }
        }

        // --- Active Tab Content Area ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (selectedTabIndex) {
                0 -> VideoLessonTab(
                    day = day,
                    currentVideoUrl = currentVideoUrl,
                    selectedLanguage = uiState.selectedLanguage,
                    languages = languages,
                    resources = uiState.phaseResources,
                    onLanguageChange = { lang ->
                        userCustomVideoUrl = null
                        viewModel.changeResourceLanguage(day.phaseId, lang)
                    },
                    onSelectVideoResource = { url ->
                        userCustomVideoUrl = url
                        Toast.makeText(context, "Playing video in app player", Toast.LENGTH_SHORT).show()
                    },
                    onSelectArticleResource = { url ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onGoToPractice = { selectedTabIndex = 2 }
                )

                1 -> ConceptsTab(
                    day = day
                )

                2 -> PracticeLabTab(
                    day = day,
                    isLoading = uiState.isLoading,
                    onCompleteDay = {
                        viewModel.completeDay(day.id) {
                            activeDay = activeDay?.copy(isCompleted = true)
                        }
                    },
                    hasNextDay = hasNextDay,
                    onGoNextDay = {
                        if (nextDay != null) {
                            userCustomVideoUrl = null
                            activeDay = nextDay
                            viewModel.loadPhaseResources(nextDay.phaseId, uiState.selectedLanguage)
                            selectedTabIndex = 0
                        }
                    }
                )

                3 -> AIAssistantTab(
                    day = day,
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

/**
 * Tab 1: Video Lesson — In-App Player with 3-language switcher and curated video index.
 */
@Composable
private fun VideoLessonTab(
    day: RoadmapDay,
    currentVideoUrl: String?,
    selectedLanguage: String,
    languages: List<String>,
    resources: List<RoadmapResource>,
    onLanguageChange: (String) -> Unit,
    onSelectVideoResource: (String) -> Unit,
    onSelectArticleResource: (String) -> Unit,
    onGoToPractice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Embedded In-App Video Player
        InAppVideoPlayer(
            videoUrl = currentVideoUrl,
            topic = day.topic,
            language = selectedLanguage,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Language Switcher Chips
        Text(
            text = "Video Audio Language:",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { lang ->
                val isSelected = lang == selectedLanguage
                val label = when (lang) {
                    "Telugu" -> "తెలుగు (Telugu)"
                    "Hindi" -> "हिन्दी (Hindi)"
                    else -> "English"
                }

                Surface(
                    shape = JobPilotShapes.small,
                    color = if (isSelected) Orange500 else MaterialTheme.cardBg,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) Orange500 else MaterialTheme.cardBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLanguageChange(lang) }
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.textPrimary,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Lesson Overview Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.cardBg
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Curated Engineering Video",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = Orange500
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = day.topic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.textPrimary
                )

                if (!day.learningObjective.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = day.learningObjective,
                        fontSize = 12.sp,
                        color = MaterialTheme.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Related Video Lessons & Articles
        Text(
            text = "Curated Learning Materials ($selectedLanguage)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (resources.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading curated $selectedLanguage tutorials...",
                    fontSize = 12.sp,
                    color = MaterialTheme.textMuted
                )
            }
        } else {
            resources.forEach { res ->
                val isVideo = res.resourceType == "video"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(JobPilotShapes.small)
                        .background(MaterialTheme.cardBg)
                        .border(1.dp, MaterialTheme.cardBorder, JobPilotShapes.small)
                        .clickable {
                            if (isVideo) {
                                onSelectVideoResource(res.url)
                            } else {
                                onSelectArticleResource(res.url)
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (isVideo) Icons.Default.PlayCircleOutline else Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = if (isVideo) ErrorRed else Orange500,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = res.title,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = MaterialTheme.textPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = if (isVideo) "▶ Play inside app • ${res.source}" else "Documentation • ${res.source}",
                                fontSize = 10.sp,
                                color = if (isVideo) Orange500 else MaterialTheme.textMuted
                            )
                        }
                    }

                    Icon(
                        imageVector = if (isVideo) Icons.Default.PlayArrow else Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Jump to Practice Lab Button
        OutlinedButton(
            onClick = onGoToPractice,
            modifier = Modifier.fillMaxWidth(),
            shape = JobPilotShapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange500)
        ) {
            Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Proceed to Practice Lab →")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Tab 2: Concepts & Theory — Structured learning objectives and key technical concepts.
 */
@Composable
private fun ConceptsTab(
    day: RoadmapDay
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Learning Objective Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.cardBg
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Day Objective",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Orange500
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = day.learningObjective ?: "Master key fundamentals and practical patterns for ${day.topic}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.textPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Key Concepts breakdown
        Text(
            text = "Key Subtopics & Theory",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (day.subtopics.isEmpty()) {
            Text(
                text = "Fundamental architecture and engineering principles for ${day.topic}.",
                fontSize = 13.sp,
                color = MaterialTheme.textSecondary
            )
        } else {
            day.subtopics.forEachIndexed { index, subtopic ->
                Surface(
                    shape = JobPilotShapes.medium,
                    color = MaterialTheme.cardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = JobPilotShapes.small,
                            color = Orange50,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Orange600
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = subtopic,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.textPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Essential knowledge tested in technical interviews and production applications.",
                                fontSize = 11.sp,
                                color = MaterialTheme.textSecondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Retention Card
        Surface(
            shape = JobPilotShapes.medium,
            color = Orange50,
            border = androidx.compose.foundation.BorderStroke(1.dp, Orange200),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💡 Daily Study Strategy",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Orange600
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Watch the video lesson first to understand visual code flow.\n2. Review subtopics above.\n3. Solve the Practice Lab coding tasks before marking this day complete.",
                    fontSize = 11.sp,
                    color = MaterialTheme.textSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/**
 * Tab 3: Practice Lab — Coding exercises, expected output terminal, and completion action.
 */
@Composable
private fun PracticeLabTab(
    day: RoadmapDay,
    isLoading: Boolean,
    onCompleteDay: () -> Unit,
    hasNextDay: Boolean,
    onGoNextDay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = null,
                tint = Orange500,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Daily Hands-on Tasks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (day.practiceTasks.isEmpty()) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.cardBg
            ) {
                Text(
                    text = "Implement a comprehensive prototype practicing ${day.topic}. Test edge cases and time complexities.",
                    fontSize = 12.sp,
                    color = MaterialTheme.textSecondary
                )
            }
        } else {
            day.practiceTasks.forEachIndexed { idx, task ->
                PracticeTaskCard(taskNumber = idx + 1, task = task)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Complete Day CTA Card
        if (!day.isCompleted) {
            JobPilotButton(
                text = if (isLoading) "Saving Progress..." else "✓ Mark Day ${day.dayNumber} as Completed",
                onClick = onCompleteDay,
                enabled = !isLoading
            )
        } else {
            Surface(
                shape = JobPilotShapes.medium,
                color = SuccessGreenBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Day ${day.dayNumber} Completed!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SuccessGreen
                        )
                    }

                    if (hasNextDay) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onGoNextDay,
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                            shape = JobPilotShapes.small
                        ) {
                            Text("Continue to Next Day →", color = BgWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

/**
 * Tab 4: AI Doubt Tutor — In-app interactive learning assistant.
 */
@Composable
private fun AIAssistantTab(
    day: RoadmapDay,
    uiState: com.jobpilot.app.ui.viewmodel.RoadmapUiState,
    viewModel: RoadmapViewModel
) {
    var userQuestion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "AI Curriculum Assistant",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.textPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Ask questions, request code snippets, or clarify doubts specifically on ${day.topic}.",
            fontSize = 12.sp,
            color = MaterialTheme.textSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Suggestion Chips
        val promptSuggestions = listOf(
            "Explain with practical example",
            "Top interview questions",
            "How to test edge cases?"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            promptSuggestions.forEach { suggestion ->
                Surface(
                    shape = JobPilotShapes.small,
                    color = Orange50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Orange200),
                    modifier = Modifier.clickable {
                        userQuestion = "$suggestion on ${day.topic}"
                        viewModel.askCurriculumAssistant(day.topic, "$suggestion on ${day.topic}", day.dayNumber)
                    }
                ) {
                    Text(
                        text = suggestion,
                        fontSize = 10.sp,
                        color = Orange600,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.cardBg
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = userQuestion,
                    onValueChange = { userQuestion = it },
                    placeholder = { Text("Ask your doubt about ${day.topic}...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    singleLine = false,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (userQuestion.isNotBlank()) {
                                viewModel.askCurriculumAssistant(day.topic, userQuestion, day.dayNumber)
                            }
                        },
                        enabled = userQuestion.isNotBlank() && !uiState.isAssistantLoading,
                        shape = JobPilotShapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        if (uiState.isAssistantLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Thinking...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask Tutor", fontSize = 12.sp)
                        }
                    }
                }

                if (uiState.assistantAnswer != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = JobPilotShapes.medium,
                        color = MaterialTheme.cardBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Tutor Explanation", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Orange600)
                                IconButton(
                                    onClick = { viewModel.clearAssistantAnswer() },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.assistantAnswer ?: "",
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeTaskCard(
    taskNumber: Int,
    task: PracticeTask
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.cardBg
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = JobPilotShapes.small,
                    color = Orange50,
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$taskNumber",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Orange600
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.description,
                fontSize = 12.sp,
                color = MaterialTheme.textSecondary
            )

            if (!task.expectedOutput.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(JobPilotShapes.small)
                        .background(Slate100)
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Expected Output / Terminal:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange500
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.expectedOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = MaterialTheme.textPrimary
                        )
                    }
                }
            }
        }
    }
}
