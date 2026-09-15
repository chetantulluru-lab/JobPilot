package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ProfileRepository {
    val profileStream: Flow<CareerProfile>
    fun getProfile(): CareerProfile
    suspend fun updatePersonalInfo(personalInfo: PersonalInfo)
    suspend fun uploadProfilePhoto(bytes: ByteArray, filename: String): Result<String>
    suspend fun deleteProfilePhoto(): Result<Unit>
    suspend fun refreshProfile(): Result<CareerProfile>
    suspend fun addEducation(education: Education)
    suspend fun updateEducation(education: Education)
    suspend fun removeEducation(educationId: String)
    suspend fun addSkill(skill: Skill)
    suspend fun removeSkill(skillId: String)
    suspend fun addExperience(experience: Experience)
    suspend fun updateExperience(experience: Experience)
    suspend fun removeExperience(experienceId: String)
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun removeProject(projectId: String)
    suspend fun addCertification(certification: Certification)
    suspend fun updateCertification(certification: Certification)
    suspend fun removeCertification(certificationId: String)
    suspend fun updateJobPreferences(preferences: JobPreference)
}

class MockProfileRepository : ProfileRepository {
    private val _profile = MutableStateFlow(MockDataProvider.currentProfile)
    override val profileStream: Flow<CareerProfile> = _profile.asStateFlow()

    override fun getProfile(): CareerProfile = _profile.value

    override suspend fun updatePersonalInfo(personalInfo: PersonalInfo) {
        _profile.update { it.copy(personalInfo = personalInfo) }
    }

    override suspend fun uploadProfilePhoto(bytes: ByteArray, filename: String): Result<String> {
        return Result.success("https://placeholder.dev/avatar.jpg")
    }

    override suspend fun deleteProfilePhoto(): Result<Unit> {
        _profile.update { it.copy(personalInfo = it.personalInfo.copy(avatarUrl = null)) }
        return Result.success(Unit)
    }

    override suspend fun refreshProfile(): Result<CareerProfile> {
        return Result.success(_profile.value)
    }

    override suspend fun addEducation(education: Education) {
        _profile.update { it.copy(education = it.education + education) }
    }

    override suspend fun updateEducation(education: Education) {
        _profile.update { it.copy(education = it.education.map { edu -> if (edu.id == education.id) education else edu }) }
    }

    override suspend fun removeEducation(educationId: String) {
        _profile.update { it.copy(education = it.education.filterNot { edu -> edu.id == educationId }) }
    }

    override suspend fun addSkill(skill: Skill) {
        _profile.update { it.copy(skills = it.skills + skill) }
    }

    override suspend fun removeSkill(skillId: String) {
        _profile.update { it.copy(skills = it.skills.filterNot { s -> s.id == skillId }) }
    }

    override suspend fun addExperience(experience: Experience) {
        _profile.update { it.copy(experience = it.experience + experience) }
    }

    override suspend fun updateExperience(experience: Experience) {
        _profile.update { it.copy(experience = it.experience.map { exp -> if (exp.id == experience.id) experience else exp }) }
    }

    override suspend fun removeExperience(experienceId: String) {
        _profile.update { it.copy(experience = it.experience.filterNot { exp -> exp.id == experienceId }) }
    }

    override suspend fun addProject(project: Project) {
        _profile.update { it.copy(projects = it.projects + project) }
    }

    override suspend fun updateProject(project: Project) {
        _profile.update { it.copy(projects = it.projects.map { p -> if (p.id == project.id) project else p }) }
    }

    override suspend fun removeProject(projectId: String) {
        _profile.update { it.copy(projects = it.projects.filterNot { p -> p.id == projectId }) }
    }

    override suspend fun addCertification(certification: Certification) {
        _profile.update { it.copy(certifications = it.certifications + certification) }
    }

    override suspend fun updateCertification(certification: Certification) {
        _profile.update { it.copy(certifications = it.certifications.map { c -> if (c.id == certification.id) certification else c }) }
    }

    override suspend fun removeCertification(certificationId: String) {
        _profile.update { it.copy(certifications = it.certifications.filterNot { c -> c.id == certificationId }) }
    }

    override suspend fun updateJobPreferences(preferences: JobPreference) {
        _profile.update { it.copy(jobPreferences = preferences) }
    }
}
