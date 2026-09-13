package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.ApplicationStatus
import com.jobpilot.app.data.model.JobApplication
import com.jobpilot.app.data.repository.ApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ApplicationUiState(
    val applications: List<JobApplication> = emptyList(),
    val filteredApplications: List<JobApplication> = emptyList(),
    val selectedStatusFilter: ApplicationStatus? = null,
    val selectedApplication: JobApplication? = null,
    val isLoading: Boolean = false
)

class ApplicationViewModel(
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            applicationRepository.applicationsStream.collect { list ->
                _uiState.value = _uiState.value.copy(
                    applications = list,
                    filteredApplications = filterByStatus(list, _uiState.value.selectedStatusFilter)
                )
            }
        }
    }

    fun selectStatusFilter(status: ApplicationStatus?) {
        _uiState.value = _uiState.value.copy(
            selectedStatusFilter = status,
            filteredApplications = filterByStatus(_uiState.value.applications, status)
        )
    }

    fun selectApplicationById(id: String) {
        val app = applicationRepository.getApplicationById(id)
        _uiState.value = _uiState.value.copy(selectedApplication = app)
    }

    private fun filterByStatus(list: List<JobApplication>, status: ApplicationStatus?): List<JobApplication> {
        return if (status == null) list else list.filter { it.currentStatus == status }
    }
}
