package com.jobpilot.app.data.model

data class CareerProfile(
    val id: String = "profile-1",
    val userId: String = "user-1",
    val personalInfo: PersonalInfo,
    val education: List<Education> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val experience: List<Experience> = emptyList(),
    val projects: List<Project> = emptyList(),
    val certifications: List<Certification> = emptyList(),
    val socialProfiles: List<SocialProfile> = emptyList(),
    val jobPreferences: JobPreference,
    val profileStrengthScore: Int = 86
)

data class PersonalInfo(
    val fullName: String,
    val email: String,
    val phone: String,
    val location: String,
    val professionalSummary: String
)

data class Education(
    val id: String,
    val degree: String,
    val college: String,
    val branch: String,
    val startDate: String,
    val endDate: String,
    val grade: String // CGPA or Percentage
)

enum class SkillCategory {
    PROGRAMMING_LANGUAGE,
    FRAMEWORK,
    DATABASE,
    TOOL,
    CLOUD,
    OTHER
}

data class Skill(
    val id: String,
    val name: String,
    val category: SkillCategory,
    val proficiencyLevel: String = "Proficient" // Beginner, Intermediate, Proficient, Expert
)

data class Experience(
    val id: String,
    val company: String,
    val role: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val technologies: List<String>
)

data class Project(
    val id: String,
    val name: String,
    val description: String,
    val technologies: List<String>,
    val startDate: String,
    val endDate: String,
    val githubUrl: String? = null,
    val liveUrl: String? = null
)

data class Certification(
    val id: String,
    val name: String,
    val issuingOrganization: String,
    val date: String,
    val credentialUrl: String? = null
)

data class SocialProfile(
    val id: String,
    val platform: String, // GitHub, LinkedIn, Portfolio, Other
    val url: String,
    val isVerified: Boolean = false
)

data class JobPreference(
    val targetRoles: List<String>,
    val preferredLocations: List<String>,
    val workMode: String, // Remote, Hybrid, On-site, Any
    val employmentType: String, // Full-time, Internship, Contract
    val salaryExpectation: String,
    val preferredTechnologies: List<String>
)
