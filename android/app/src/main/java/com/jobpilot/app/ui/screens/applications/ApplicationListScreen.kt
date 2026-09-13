package com.jobpilot.app.ui.screens.applications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobpilot.app.data.model.ApplicationStatus
import com.jobpilot.app.data.model.JobApplication
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationListScreen(
    viewModel: ApplicationViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application Tracking", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Filter Tabs
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = uiState.selectedStatusFilter == null,
                            onClick = { viewModel.selectStatusFilter(null) },
                            label = { Text("All (${uiState.applications.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Orange500,
                                selectedLabelColor = BgWhite
                            )
                        )
                    }

                    items(ApplicationStatus.values()) { status ->
                        val count = uiState.applications.count { it.currentStatus == status }
                        if (count > 0) {
                            FilterChip(
                                selected = uiState.selectedStatusFilter == status,
                                onClick = { viewModel.selectStatusFilter(status) },
                                label = { Text("${status.displayName} ($count)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Orange500,
                                    selectedLabelColor = BgWhite
                                )
                            )
                        }
                    }
                }
            }

            if (uiState.filteredApplications.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Applications in this Stage",
                        message = "Explore recommended opportunities and submit applications using your verified Career Profile."
                    )
                }
            } else {
                items(uiState.filteredApplications) { app ->
                    ApplicationCardItem(app = app, onClick = { onNavigateToDetail(app.id) })
                }
            }
        }
    }
}

@Composable
fun ApplicationCardItem(
    app: JobApplication,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = BgWhite,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.jobTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${app.company} • ${app.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }

                Surface(
                    shape = ChipShape,
                    color = when (app.currentStatus) {
                        ApplicationStatus.INTERVIEW -> InfoBlueBg
                        ApplicationStatus.ASSESSMENT -> WarningAmberBg
                        ApplicationStatus.OFFER -> SuccessGreenBg
                        else -> Slate100
                    }
                ) {
                    Text(
                        text = app.currentStatus.displayName,
                        style = JobPilotTypography.labelMedium,
                        color = when (app.currentStatus) {
                            ApplicationStatus.INTERVIEW -> InfoBlue
                            ApplicationStatus.ASSESSMENT -> WarningAmber
                            ApplicationStatus.OFFER -> SuccessGreen
                            else -> Slate700
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Applied: ${app.appliedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )
                Text(
                    text = "Match at apply: ${app.matchScoreAtApplication}%",
                    style = JobPilotTypography.labelMedium,
                    color = Orange600
                )
            }
        }
    }
}
