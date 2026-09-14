package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.repository.ProfileRepository
import com.jobpilot.app.data.repository.RoadmapRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RoadmapUiState(
    val roadmaps: List<RoadmapSummary> = emptyList(),
    val currentRoadmap: RoadmapDetail? = null,
    val selectedDay: RoadmapDay? = null,
    val phaseResources: List<RoadmapResource> = emptyList(),
    val selectedLanguage: String = "English",
    val searchSuggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val errorMessage: String? = null,
    val roadmapCompletedEvent: Boolean = false,
    val completedRoadmapSkills: List<String> = emptyList(),
    val skillsAddedMessage: String? = null
)

class RoadmapViewModel(
    private val roadmapRepository: RoadmapRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoadmapUiState())
    val uiState: StateFlow<RoadmapUiState> = _uiState.asStateFlow()

    private var suggestionJob: Job? = null

    init {
        loadRoadmaps()
    }

    fun loadRoadmaps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = roadmapRepository.getRoadmaps()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    roadmaps = result.getOrDefault(emptyList()),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to load roadmaps"
                )
            }
        }
    }

    fun onQueryChanged(query: String) {
        suggestionJob?.cancel()
        if (query.trim().length < 1) {
            _uiState.value = _uiState.value.copy(searchSuggestions = emptyList())
            return
        }
        suggestionJob = viewModelScope.launch {
            delay(250)
            val result = roadmapRepository.getSuggestions(query.trim())
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(searchSuggestions = result.getOrDefault(emptyList()))
            }
        }
    }

    fun generateRoadmap(goal: String, duration: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, errorMessage = null)
            val result = roadmapRepository.generateRoadmap(goal = goal.trim(), duration = duration)
            if (result.isSuccess) {
                val detail = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    currentRoadmap = detail,
                    errorMessage = null
                )
                loadRoadmaps()
                onSuccess(detail.id)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Roadmap couldn't be generated. Please try again."
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    errorMessage = errorMsg
                )
            }
        }
    }

    fun loadRoadmapDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = roadmapRepository.getRoadmapById(id)
            if (result.isSuccess) {
                val detail = result.getOrNull()
                _uiState.value = _uiState.value.copy(
                    currentRoadmap = detail,
                    isLoading = false
                )
                // If there is an active day, load phase resources for it
                detail?.phases?.firstOrNull { it.isUnlocked }?.let { phase ->
                    loadPhaseResources(phase.id, _uiState.value.selectedLanguage)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to load roadmap details"
                )
            }
        }
    }

    fun selectDay(day: RoadmapDay) {
        _uiState.value = _uiState.value.copy(selectedDay = day)
        loadPhaseResources(day.phaseId, _uiState.value.selectedLanguage)
    }

    fun loadPhaseResources(phaseId: String, language: String = "English") {
        viewModelScope.launch {
            val result = roadmapRepository.getPhaseResources(phaseId, language)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    phaseResources = result.getOrDefault(emptyList()),
                    selectedLanguage = language
                )
            }
        }
    }

    fun changeResourceLanguage(phaseId: String, language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
        loadPhaseResources(phaseId, language)
    }

    fun completeDay(dayId: String, onCompleted: (DayCompleteResult) -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = roadmapRepository.completeDay(dayId)
            if (result.isSuccess) {
                val res = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    roadmapCompletedEvent = res.roadmapCompleted,
                    completedRoadmapSkills = res.skillsLearned
                )

                // Refresh roadmap details and user profile for streaks
                _uiState.value.currentRoadmap?.id?.let { loadRoadmapDetail(it) }
                profileRepository.refreshProfile()
                loadRoadmaps()

                onCompleted(res)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to complete day"
                )
            }
        }
    }

    fun addSkillsToResume(roadmapId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = roadmapRepository.addSkillsToResume(roadmapId)
            if (result.isSuccess) {
                val addRes = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    skillsAddedMessage = addRes.message,
                    roadmapCompletedEvent = false
                )
                profileRepository.refreshProfile()
                onDone()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to add skills"
                )
            }
        }
    }

    fun dismissRoadmapCompletedModal() {
        _uiState.value = _uiState.value.copy(roadmapCompletedEvent = false)
    }

    fun deleteRoadmap(roadmapId: String, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            val result = roadmapRepository.deleteRoadmap(roadmapId)
            if (result.isSuccess) {
                loadRoadmaps()
                onDeleted()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
