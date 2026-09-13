package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: CareerProfile,
    val missingFields: List<MissingField> = emptyList(),
    val isSaving: Boolean = false,
    val feedbackMessage: String? = null
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(
            profile = profileRepository.getProfile(),
            missingFields = MockDataProvider.mockParsedResume.missingFields
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profileStream.collect { updatedProfile ->
                _uiState.value = _uiState.value.copy(profile = updatedProfile)
            }
        }
    }

    fun updatePersonalInfo(info: PersonalInfo) {
        viewModelScope.launch {
            profileRepository.updatePersonalInfo(info)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Personal information updated")
        }
    }

    fun addEducation(education: Education) {
        viewModelScope.launch {
            profileRepository.addEducation(education)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Education record added")
        }
    }

    fun removeEducation(id: String) {
        viewModelScope.launch {
            profileRepository.removeEducation(id)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Education record removed")
        }
    }

    fun addSkill(name: String, category: SkillCategory) {
        viewModelScope.launch {
            val skill = Skill(
                id = "s-${System.currentTimeMillis()}",
                name = name.trim(),
                category = category
            )
            profileRepository.addSkill(skill)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Added skill: $name")
        }
    }

    fun removeSkill(id: String) {
        viewModelScope.launch {
            profileRepository.removeSkill(id)
        }
    }

    fun addExperience(exp: Experience) {
        viewModelScope.launch {
            profileRepository.addExperience(exp)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Experience added")
        }
    }

    fun removeExperience(id: String) {
        viewModelScope.launch {
            profileRepository.removeExperience(id)
        }
    }

    fun addProject(proj: Project) {
        viewModelScope.launch {
            profileRepository.addProject(proj)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Project added")
        }
    }

    fun removeProject(id: String) {
        viewModelScope.launch {
            profileRepository.removeProject(id)
        }
    }

    fun addCertification(cert: Certification) {
        viewModelScope.launch {
            profileRepository.addCertification(cert)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Certification added")
        }
    }

    fun removeCertification(id: String) {
        viewModelScope.launch {
            profileRepository.removeCertification(id)
        }
    }

    fun updatePreferences(pref: JobPreference) {
        viewModelScope.launch {
            profileRepository.updateJobPreferences(pref)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Job preferences saved")
        }
    }

    fun clearFeedback() {
        _uiState.value = _uiState.value.copy(feedbackMessage = null)
    }
}
