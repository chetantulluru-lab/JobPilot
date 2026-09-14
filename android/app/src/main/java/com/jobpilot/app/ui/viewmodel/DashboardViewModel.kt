package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.repository.ProfileRepository
import com.jobpilot.app.data.repository.RoadmapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val userName: String = "",
    val profileStrength: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val roadmaps: List<RoadmapSummary> = emptyList(),
    val activeRoadmap: RoadmapDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val roadmapRepository: RoadmapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeProfile()
        loadDashboardData()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            profileRepository.profileStream.collect { profile ->
                _uiState.value = _uiState.value.copy(
                    userName = profile.personalInfo.fullName.ifBlank { "Candidate" },
                    profileStrength = profile.profileStrengthScore,
                    currentStreak = profile.currentStreak,
                    longestStreak = profile.longestStreak
                )
            }
        }
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            profileRepository.refreshProfile()
            val roadmapsResult = roadmapRepository.getRoadmaps()
            if (roadmapsResult.isSuccess) {
                val list = roadmapsResult.getOrDefault(emptyList())
                _uiState.value = _uiState.value.copy(
                    roadmaps = list,
                    isLoading = false
                )

                // Load first active roadmap details if available
                val firstActive = list.firstOrNull { !it.isCompleted } ?: list.firstOrNull()
                if (firstActive != null) {
                    val detailResult = roadmapRepository.getRoadmapById(firstActive.id)
                    if (detailResult.isSuccess) {
                        _uiState.value = _uiState.value.copy(activeRoadmap = detailResult.getOrNull())
                    }
                } else {
                    _uiState.value = _uiState.value.copy(activeRoadmap = null)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = roadmapsResult.exceptionOrNull()?.message
                )
            }
        }
    }
}
