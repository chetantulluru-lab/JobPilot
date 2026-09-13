package com.jobpilot.app.ui.screens.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobpilot.app.data.model.Job
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    viewModel: JobViewModel,
    onNavigateToJobDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterModes = listOf("All", "Remote", "Hybrid", "On-site")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Job Matching", style = MaterialTheme.typography.headlineMedium) },
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
            // Search Input
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Search by role, company, or skill...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.large,
                    singleLine = true
                )
            }

            // Work Mode Filter Chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filterModes) { mode ->
                        val isSelected = uiState.selectedWorkMode == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onWorkModeSelected(mode) },
                            label = { Text(mode) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Orange500,
                                selectedLabelColor = BgWhite
                            )
                        )
                    }
                }
            }

            // Results count
            item {
                Text(
                    text = "${uiState.filteredJobs.size} Opportunities Evaluated by AI",
                    style = MaterialTheme.typography.labelLarge,
                    color = Slate500
                )
            }

            // Job Items
            if (uiState.filteredJobs.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Matching Roles",
                        message = "Try clearing search filters or changing preferred work mode.",
                        actionButtonText = "Reset Filters",
                        onActionClick = {
                            viewModel.onSearchQueryChanged("")
                            viewModel.onWorkModeSelected("All")
                        }
                    )
                }
            } else {
                items(uiState.filteredJobs) { job ->
                    JobCardItem(job = job, onClick = { onNavigateToJobDetail(job.id) })
                }
            }
        }
    }
}

@Composable
fun JobCardItem(
    job: Job,
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
                        text = job.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${job.company} • ${job.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                    Text(
                        text = "${job.employmentType} • ${job.stipendOrSalary}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Orange600
                    )
                }

                MatchScoreBadge(score = job.matchDetails.matchScore)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills breakdown preview
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                job.matchDetails.strongMatches.take(3).forEach { skill ->
                    SkillChip(skillName = skill, variant = ChipVariant.STRONG_MATCH)
                }
                if (job.matchDetails.missingSkills.isNotEmpty()) {
                    SkillChip(
                        skillName = job.matchDetails.missingSkills.first(),
                        variant = ChipVariant.MISSING_GAP
                    )
                }
            }
        }
    }
}
