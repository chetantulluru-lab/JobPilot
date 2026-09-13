package com.jobpilot.app.ui.screens.applications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.SectionHeader
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationDetailScreen(
    appId: String,
    viewModel: ApplicationViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(appId) {
        viewModel.selectApplicationById(appId)
    }

    val app = uiState.selectedApplication

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application Details", style = MaterialTheme.typography.titleLarge) },
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
        if (app == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Orange500)
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
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column {
                            Text(text = app.jobTitle, style = MaterialTheme.typography.headlineLarge, color = Slate900)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${app.company} • ${app.location}", style = MaterialTheme.typography.bodyMedium, color = Slate500)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Current Status:", style = MaterialTheme.typography.bodySmall, color = Slate500)
                                Text(
                                    text = app.currentStatus.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Orange600,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Recruiter & Notes
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (app.recruiterContact != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Recruiter: ${app.recruiterContact}", style = MaterialTheme.typography.bodyMedium, color = Slate800)
                                }
                            }
                            if (app.notes.isNotBlank()) {
                                Text(text = "Notes: ${app.notes}", style = MaterialTheme.typography.bodyMedium, color = Slate600)
                            }
                        }
                    }
                }

                // Progress Timeline
                item {
                    SectionHeader(
                        title = "Progress Timeline",
                        subtitle = "Milestones across recruitment workflow"
                    )
                }

                itemsIndexed(app.timeline) { index, event ->
                    val isLast = index == app.timeline.size - 1

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Timeline vertical line + bubble
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(36.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (event.isCompleted) Orange500 else Slate200),
                                contentAlignment = Alignment.Center
                            ) {
                                if (event.isCompleted) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                            if (!isLast) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(50.dp)
                                        .background(Orange200)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Event content
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = event.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Slate900
                                )
                                Text(
                                    text = event.timestamp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate600,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }
            }
        }
    }
}
