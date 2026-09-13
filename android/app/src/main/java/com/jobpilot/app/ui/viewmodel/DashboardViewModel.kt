package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.repository.ApplicationRepository
import com.jobpilot.app.data.repository.JobRepository
import com.jobpilot.app.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val userName: String = "Chetan",
    val profileStrength: Int = 86,
    val totalJobMatches: Int = 12,
    val totalApplications: Int = 5,
    val totalInterviews: Int = 2,
    val aiInsight: String = "Your profile is strong for Python Developer and Backend Intern roles. Adding FastAPI and Docker could increase your match rate by +14%.",
    val recommendedJobs: List<Job> = emptyList(),
    val recentApplications: List<JobApplication> = emptyList(),
    val topSkillGaps: List<String> = listOf("Docker", "FastAPI", "AWS"),
    val resumeStatusText: String = "Active (Software Engineer Modern)",
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = profileRepository.getProfile()
            val jobs = jobRepository.getAllJobs()
            val apps = applicationRepository.getAllApplications()

            val interviewsCount = apps.count { it.currentStatus == ApplicationStatus.INTERVIEW }

            _uiState.value = _uiState.value.copy(
                userName = profile.personalInfo.fullName.ifBlank { "Candidate" },
                profileStrength = profile.profileStrengthScore,
                totalJobMatches = jobs.size,
                totalApplications = apps.size,
                totalInterviews = interviewsCount,
                recommendedJobs = jobs.take(3),
                recentApplications = apps.take(3),
                isLoading = false
            )
        }
    }
}
