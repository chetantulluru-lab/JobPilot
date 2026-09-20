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
import com.jobpilot.app.data.model.*
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.*

private fun launchYouTubeVideo(context: android.content.Context, videoUrl: String?) {
    val url = videoUrl?.takeIf { it.isNotBlank() } ?: "https://www.youtube.com"
    try {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage("com.google.android.youtube")
        }
        context.startActivity(appIntent)
    } catch (_: Exception) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(webIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "Cannot open YouTube URL", Toast.LENGTH_SHORT).show()
        }
    }
}

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
                    viewModel.loadDayQuiz(found.id)
                    viewModel.loadDayNote(found.id)
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

    val tabTitles = listOf("🎥 Video", "📖 Concepts", "💻 Practice", "🏆 Quiz", "📝 Notes", "🤖 AI Tutor")

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

                    val isBookmarked = uiState.currentNote?.isBookmarked ?: false
                    IconButton(
                        onClick = { viewModel.toggleDayBookmark(day.id) }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Orange500 else MaterialTheme.textSecondary
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
                    resources = uiState.phaseResources,
                    onOpenVideo = { url ->
                        launchYouTubeVideo(context, url)
                    },
                    onOpenArticle = { url ->
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

                3 -> DailyQuizTab(
                    day = day,
                    uiState = uiState,
                    viewModel = viewModel,
                    onQuizCompleted = {
                        activeDay = activeDay?.copy(isCompleted = true)
                    }
                )

                4 -> PersonalNotesTab(
                    day = day,
                    uiState = uiState,
                    viewModel = viewModel
                )

                5 -> AIAssistantTab(
                    day = day,
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

/**
 * Tab 1: Video Lesson — Opens curated video directly on YouTube with one clean language.
 */
@Composable
private fun VideoLessonTab(
    day: RoadmapDay,
    currentVideoUrl: String?,
    resources: List<RoadmapResource>,
    onOpenVideo: (String) -> Unit,
    onOpenArticle: (String) -> Unit,
    onGoToPractice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Direct YouTube Hero Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            fontSize = 12.sp,
                            color = Orange500
                        )
                    }

                    Surface(
                        shape = JobPilotShapes.small,
                        color = Color(0xFFFFEBEB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
                    ) {
                        Text(
                            text = "YouTube HD",
                            color = Color(0xFFD32F2F),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = day.topic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.textPrimary
                )

                if (!day.learningObjective.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day.learningObjective,
                        fontSize = 13.sp,
                        color = MaterialTheme.textSecondary,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Big prominent direct YouTube launcher button
                Button(
                    onClick = { onOpenVideo(currentVideoUrl ?: "https://www.youtube.com") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = JobPilotShapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Watch Video on YouTube",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Opens directly on YouTube to start playing immediately without search results.",
                    fontSize = 11.sp,
                    color = MaterialTheme.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Related Video Lessons & Articles
        Text(
            text = "Curated Learning Materials",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.textPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (resources.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading curated tutorials...",
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
                                onOpenVideo(res.url)
                            } else {
                                onOpenArticle(res.url)
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
                            tint = if (isVideo) Color(0xFFE50914) else Orange500,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = res.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.textPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = if (isVideo) "▶ Watch on YouTube • ${res.source}" else "Documentation • ${res.source}",
                                fontSize = 11.sp,
                                color = if (isVideo) Orange500 else MaterialTheme.textMuted
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
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

/**
 * Tab 4: Daily Quiz & Gamified Streaks
 */
@Composable
private fun DailyQuizTab(
    day: RoadmapDay,
    uiState: RoadmapUiState,
    viewModel: RoadmapViewModel,
    onQuizCompleted: () -> Unit
) {
    val quiz = uiState.dailyQuiz
    val quizResult = uiState.quizResult
    val selectedAnswers = remember(day.id) { mutableStateMapOf<Int, Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quiz Header Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Orange50.copy(alpha = 0.7f),
            borderColor = Orange300.copy(alpha = 0.5f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Daily Knowledge Check & Streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Orange700
                    )
                    Text(
                        text = "Pass with 60%+ to extend your daily streak and mark Day ${day.dayNumber} complete!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate700
                    )
                }
            }
        }

        if (uiState.isQuizLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                LoadingState(message = "Loading interactive questions...")
            }
        } else if (quizResult != null) {
            // Result View
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = BgWhite
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (quizResult.passed) "🎉 Streak Extended!" else "Keep Trying!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quizResult.passed) SuccessGreen else GapOrange
                    )
                    Text(
                        text = "${quizResult.scorePercentage}% Score (${quizResult.correctAnswers}/${quizResult.totalQuestions} Correct)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate800
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥 Streak: ${quizResult.currentStreak} Days", fontWeight = FontWeight.Bold, color = Orange600, fontSize = 14.sp)
                    }
                    Text(
                        text = quizResult.feedback,
                        fontSize = 12.sp,
                        color = Slate600,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            selectedAnswers.clear()
                            viewModel.clearQuizResult()
                            viewModel.loadDayQuiz(day.id)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Retake Quiz")
                    }
                }
            }

            // Question Details Breakdown
            SectionHeader(title = "Detailed Review & Explanations")
            quizResult.questionResults.forEachIndexed { idx, qr ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Question ${idx + 1}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                            Text(
                                text = if (qr.isCorrect) "✓ Correct" else "✗ Incorrect",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (qr.isCorrect) SuccessGreen else GapOrange
                            )
                        }
                        Divider(color = Slate100)
                        Text(
                            text = qr.explanation,
                            fontSize = 12.sp,
                            color = Slate700,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        } else if (quiz != null) {
            // Unsubmitted Question Form
            quiz.questions.forEachIndexed { qIdx, question ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "${qIdx + 1}. ${question.question}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            fontSize = 14.sp
                        )

                        question.options.forEachIndexed { optIdx, optText ->
                            val isSelected = selectedAnswers[question.id] == optIdx
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Orange50 else Slate100.copy(alpha = 0.5f))
                                    .border(1.dp, if (isSelected) Orange500 else Color.Transparent, androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                    .clickable { selectedAnswers[question.id] = optIdx }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedAnswers[question.id] = optIdx },
                                    colors = RadioButtonDefaults.colors(selectedColor = Orange500)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = optText,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Orange700 else Slate800
                                )
                            }
                        }
                    }
                }
            }

            // Submit Button
            val allAnswered = quiz.questions.all { selectedAnswers.containsKey(it.id) }
            Button(
                onClick = {
                    viewModel.submitDayQuiz(day.id, selectedAnswers.toMap(), onDayCompleted = onQuizCompleted)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = allAnswered && !uiState.isQuizSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = Orange500)
            ) {
                if (uiState.isQuizSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text(if (allAnswered) "Submit Quiz & Boost Streak 🔥" else "Answer All Questions to Submit")
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { viewModel.loadDayQuiz(day.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Load Daily Quiz")
                }
            }
        }
    }
}

/**
 * Tab 5: Personal Notes & Bookmark Hub
 */
@Composable
private fun PersonalNotesTab(
    day: RoadmapDay,
    uiState: RoadmapUiState,
    viewModel: RoadmapViewModel
) {
    val context = LocalContext.current
    val currentNote = uiState.currentNote
    var noteText by remember(day.id, currentNote?.noteText) {
        mutableStateOf(currentNote?.noteText ?: "")
    }
    val isBookmarked = currentNote?.isBookmarked ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info Banner
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
                    Text("Day ${day.dayNumber} Personal Notes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                    Text("Capture your key learnings, snippets, and questions for revision.", fontSize = 11.sp, color = Slate500)
                }

                IconButton(onClick = { viewModel.toggleDayBookmark(day.id) }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Toggle Bookmark",
                        tint = if (isBookmarked) Orange500 else Slate400
                    )
                }
            }
        }

        // Notes Editor
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 220.dp, max = 340.dp),
            placeholder = {
                Text(
                    "Write your notes here...\n\n• Key concepts learned\n• Code snippets\n• Things to review before interviews",
                    fontSize = 13.sp,
                    color = Slate400
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange500,
                unfocusedBorderColor = Slate300,
                focusedContainerColor = BgWhite,
                unfocusedContainerColor = BgWhite
            )
        )

        // Save Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    viewModel.saveDayNote(day.id, noteText, isBookmarked)
                    Toast.makeText(context, "Notes saved successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                enabled = !uiState.isNoteSaving
            ) {
                if (uiState.isNoteSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving...")
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Notes")
                }
            }

            OutlinedButton(
                onClick = { viewModel.toggleDayBookmark(day.id) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isBookmarked) Orange600 else Slate700)
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isBookmarked) "Bookmarked" else "Bookmark")
            }
        }
    }
}
