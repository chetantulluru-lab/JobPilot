package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            missingFields = computeMissingFields(profileRepository.getProfile())
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profileStream.collect { updatedProfile ->
                _uiState.value = _uiState.value.copy(
                    profile = updatedProfile,
                    missingFields = computeMissingFields(updatedProfile)
                )
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

    fun updateEducation(education: Education) {
        viewModelScope.launch {
            profileRepository.updateEducation(education)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Education record updated")
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
            _uiState.value = _uiState.value.copy(feedbackMessage = "Skill removed")
        }
    }

    fun addExperience(exp: Experience) {
        viewModelScope.launch {
            profileRepository.addExperience(exp)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Experience added")
        }
    }

    fun updateExperience(exp: Experience) {
        viewModelScope.launch {
            profileRepository.updateExperience(exp)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Experience updated")
        }
    }

    fun removeExperience(id: String) {
        viewModelScope.launch {
            profileRepository.removeExperience(id)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Experience removed")
        }
    }

    fun addProject(proj: Project) {
        viewModelScope.launch {
            profileRepository.addProject(proj)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Project added")
        }
    }

    fun updateProject(proj: Project) {
        viewModelScope.launch {
            profileRepository.updateProject(proj)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Project updated")
        }
    }

    fun removeProject(id: String) {
        viewModelScope.launch {
            profileRepository.removeProject(id)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Project removed")
        }
    }

    fun addCertification(cert: Certification) {
        viewModelScope.launch {
            profileRepository.addCertification(cert)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Certification added")
        }
    }

    fun updateCertification(cert: Certification) {
        viewModelScope.launch {
            profileRepository.updateCertification(cert)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Certification updated")
        }
    }

    fun removeCertification(id: String) {
        viewModelScope.launch {
            profileRepository.removeCertification(id)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Certification removed")
        }
    }

    fun updatePreferences(pref: JobPreference) {
        viewModelScope.launch {
            profileRepository.updateJobPreferences(pref)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Job preferences saved")
        }
    }

    fun uploadProfilePhoto(bytes: ByteArray, filename: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val result = profileRepository.uploadProfilePhoto(bytes, filename)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    feedbackMessage = "Profile photo updated"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    feedbackMessage = result.exceptionOrNull()?.message ?: "Failed to upload photo"
                )
            }
        }
    }

    fun deleteProfilePhoto() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val result = profileRepository.deleteProfilePhoto()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    feedbackMessage = "Profile photo removed"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    feedbackMessage = "Failed to remove photo"
                )
            }
        }
    }

    fun refreshProfile() {
        viewModelScope.launch {
            profileRepository.refreshProfile()
        }
    }

    fun clearFeedback() {
        _uiState.value = _uiState.value.copy(feedbackMessage = null)
    }
}

private fun computeMissingFields(profile: CareerProfile): List<MissingField> {
    val list = mutableListOf<MissingField>()
    if (profile.education.isEmpty()) {
        list.add(
            MissingField(
                fieldKey = "education",
                fieldLabel = "Education",
                reason = "No formal education or degree listed",
                suggestedAction = "Add your university, degree, and graduation year"
            )
        )
    }
    if (profile.skills.isEmpty()) {
        list.add(
            MissingField(
                fieldKey = "skills",
                fieldLabel = "Key Skills",
                reason = "No technical skills detected on your profile",
                suggestedAction = "Add at least 3-5 core technologies or programming languages"
            )
        )
    }
    if (profile.projects.isEmpty() && profile.experience.isEmpty()) {
        list.add(
            MissingField(
                fieldKey = "projects",
                fieldLabel = "Projects or Experience",
                reason = "No projects or work history listed",
                suggestedAction = "Add your notable engineering projects or internships"
            )
        )
    }
    if (profile.personalInfo.phone.isBlank()) {
        list.add(
            MissingField(
                fieldKey = "phone",
                fieldLabel = "Phone Number",
                reason = "Recruiters frequently reach out via phone",
                suggestedAction = "Add a valid contact phone number"
            )
        )
    }
    return list
}
