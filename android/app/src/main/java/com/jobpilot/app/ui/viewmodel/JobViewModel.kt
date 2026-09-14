package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.Job
import com.jobpilot.app.data.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JobUiState(
    val jobs: List<Job> = emptyList(),
    val filteredJobs: List<Job> = emptyList(),
    val searchQuery: String = "",
    val selectedWorkMode: String = "All", // All, Remote, Hybrid, On-site
    val selectedJob: Job? = null,
    val isLoading: Boolean = false
)

class JobViewModel(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            jobRepository.jobsStream.collect { list ->
                _uiState.value = _uiState.value.copy(
                    jobs = list,
                    filteredJobs = if (_uiState.value.searchQuery.isBlank() && (_uiState.value.selectedWorkMode == "All" || _uiState.value.selectedWorkMode.isBlank())) list else _uiState.value.filteredJobs
                )
            }
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            jobRepository.refreshJobs()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onWorkModeSelected(mode: String) {
        _uiState.value = _uiState.value.copy(selectedWorkMode = mode)
        applyFilters()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val filtered = jobRepository.filterJobs(
                query = _uiState.value.searchQuery,
                workMode = _uiState.value.selectedWorkMode
            )
            _uiState.value = _uiState.value.copy(filteredJobs = filtered)
        }
    }

    fun selectJobById(id: String) {
        val job = jobRepository.getJobById(id)
        _uiState.value = _uiState.value.copy(selectedJob = job)
        viewModelScope.launch {
            val updatedMatch = jobRepository.getJobMatch(id)
            if (updatedMatch != null && _uiState.value.selectedJob?.id == id) {
                _uiState.value = _uiState.value.copy(
                    selectedJob = _uiState.value.selectedJob?.copy(matchDetails = updatedMatch)
                )
            }
        }
    }
}
