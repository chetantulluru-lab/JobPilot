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
        loadJobs()
    }

    private fun loadJobs() {
        val all = jobRepository.getAllJobs()
        _uiState.value = _uiState.value.copy(
            jobs = all,
            filteredJobs = all
        )
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
    }
}
