package com.jobpilot.app.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*

data class OnboardingPageData(
    val title: String,
    val subtitle: String,
    val highlightBadge: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val pages = listOf(
        OnboardingPageData(
            title = "Build your career profile",
            subtitle = "From Resume to Structured Intelligence",
            highlightBadge = "AI Resume Intelligence",
            description = "Import your existing resume or enter your details manually. JobPilot extracts skills, coursework, and projects into a unified career knowledge base."
        ),
        OnboardingPageData(
            title = "Find jobs that fit you",
            subtitle = "Semantic Matching & Skill Gap Analysis",
            highlightBadge = "Smart Job Discovery",
            description = "Compare your competencies against real-world job requirements with 0-100% relevance scores, strong match breakdowns, and clear learning paths."
        ),
        OnboardingPageData(
            title = "Stay ahead of every application",
            subtitle = "Application Tracking & Gmail Intelligence",
            highlightBadge = "Cockpit Organization",
            description = "Generate customized resumes for target roles, track applications in a clean pipeline, and monitor recruiter emails with smart notification alerts."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar: Skip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (currentPage < pages.size - 1) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.titleMedium,
                    color = Slate500,
                    modifier = Modifier
                        .clickable { onFinished() }
                        .padding(8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Center Hero Content
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "OnboardingContent"
        ) { pageIndex ->
            val page = pages[pageIndex]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AIOrb(size = 140.dp)

                Spacer(modifier = Modifier.height(32.dp))

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = page.highlightBadge,
                            style = JobPilotTypography.labelMedium,
                            color = Orange600,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = page.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Slate900,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = page.subtitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = Orange500,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = page.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate600,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // Bottom Controls: Page Dots + Next / Get Started
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Dots indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(pages.size) { index ->
                    val isSelected = currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Orange500 else Slate200)
                    )
                }
            }

            if (currentPage < pages.size - 1) {
                JobPilotButton(
                    text = "Next",
                    onClick = { currentPage++ }
                )
            } else {
                JobPilotButton(
                    text = "Get Started",
                    onClick = onFinished
                )
            }
        }
    }
}
