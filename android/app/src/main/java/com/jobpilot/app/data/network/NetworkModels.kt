package com.jobpilot.app.data.network

import com.google.gson.annotations.SerializedName

// --- Auth DTOs ---
data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("full_name") val fullName: String
)

data class TokenResponseDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_verified") val isVerified: Boolean
)

// --- Profile DTOs ---
data class EducationDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("institution") val institution: String,
    @SerializedName("degree") val degree: String,
    @SerializedName("field_of_study") val fieldOfStudy: String,
    @SerializedName("start_year") val startYear: Int? = null,
    @SerializedName("end_year") val endYear: Int? = null,
    @SerializedName("grade_or_cgpa") val gradeOrCgpa: String? = null
)

data class SkillDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String = "Technical",
    @SerializedName("proficiency") val proficiency: String = "Intermediate",
    @SerializedName("is_top_skill") val isTopSkill: Boolean = false
)

data class ExperienceDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("company") val company: String,
    @SerializedName("title") val title: String,
    @SerializedName("location") val location: String? = null,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("is_current") val isCurrent: Boolean = false,
    @SerializedName("description") val description: String? = null
)

data class ProjectDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("tech_stack") val techStack: String? = null,
    @SerializedName("github_url") val githubUrl: String? = null,
    @SerializedName("live_url") val liveUrl: String? = null,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null
)

data class CareerProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("headline") val headline: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("profile_strength") val profileStrength: Int = 50,
    @SerializedName("education") val education: List<EducationDto> = emptyList(),
    @SerializedName("skills") val skills: List<SkillDto> = emptyList(),
    @SerializedName("experience") val experience: List<ExperienceDto> = emptyList(),
    @SerializedName("projects") val projects: List<ProjectDto> = emptyList()
)

// --- Job DTOs ---
data class JobDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("company") val company: String,
    @SerializedName("location") val location: String,
    @SerializedName("work_mode") val workMode: String,
    @SerializedName("employment_type") val employmentType: String,
    @SerializedName("salary_range") val salaryRange: String? = null,
    @SerializedName("description") val description: String,
    @SerializedName("requirements") val requirements: String? = null,
    @SerializedName("skills_required") val skillsRequired: String,
    @SerializedName("preferred_skills") val preferredSkills: String? = null,
    @SerializedName("education_requirement") val educationRequirement: String? = null
)

data class JobMatchDto(
    @SerializedName("job_id") val jobId: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("company") val company: String,
    @SerializedName("match_score") val matchScore: Int,
    @SerializedName("match_tier") val matchTier: String,
    @SerializedName("matched_skills") val matchedSkills: List<String>,
    @SerializedName("missing_skills") val missingSkills: List<String>,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("strong_matches") val strongMatches: List<String> = emptyList(),
    @SerializedName("missing_required_skills") val missingRequiredSkills: List<String> = emptyList(),
    @SerializedName("missing_preferred_skills") val missingPreferredSkills: List<String> = emptyList(),
    @SerializedName("weak_skills") val weakSkills: List<String> = emptyList(),
    @SerializedName("experience_relevance") val experienceRelevance: Int = 0,
    @SerializedName("education_relevance") val educationRelevance: Int = 0,
    @SerializedName("profile_completeness") val profileCompleteness: Int = 0,
    @SerializedName("skill_gap_count") val skillGapCount: Int = 0
)

data class SkillGapItemDto(
    @SerializedName("step_number") val stepNumber: Int,
    @SerializedName("skill_name") val skillName: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("current_proficiency") val currentProficiency: String,
    @SerializedName("recommendation") val recommendation: String,
    @SerializedName("estimated_time") val estimatedTime: String? = null,
    @SerializedName("why_it_matters") val whyItMatters: String? = null,
    @SerializedName("roadmap_topics") val roadmapTopics: List<String> = emptyList(),
    @SerializedName("suggested_practice") val suggestedPractice: String? = null
)

data class SkillGapDto(
    @SerializedName("job_id") val jobId: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("match_score") val matchScore: Int,
    @SerializedName("gaps") val gaps: List<SkillGapItemDto>
)

// --- Application DTOs ---
data class ApplicationEventDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("event_date") val eventDate: String,
    @SerializedName("stage") val stage: String
)

data class ApplicationDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("job_id") val jobId: String? = null,
    @SerializedName("company") val company: String,
    @SerializedName("role") val role: String,
    @SerializedName("location") val location: String? = null,
    @SerializedName("salary") val salary: String? = null,
    @SerializedName("work_mode") val workMode: String = "Remote",
    @SerializedName("status") val status: String,
    @SerializedName("applied_date") val appliedDate: String,
    @SerializedName("next_step") val nextStep: String? = null,
    @SerializedName("recruiter_name") val recruiterName: String? = null,
    @SerializedName("recruiter_email") val recruiterEmail: String? = null,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("events") val events: List<ApplicationEventDto> = emptyList()
)

// --- Resume DTOs ---
data class ResumeDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_size_bytes") val fileSizeBytes: Int,
    @SerializedName("missing_fields") val missingFields: List<String> = emptyList(),
    @SerializedName("is_active") val isActive: Boolean
)

data class MissingFieldsAuditDto(
    @SerializedName("total_missing") val totalMissing: Int,
    @SerializedName("missing_fields") val missingFields: List<String>,
    @SerializedName("recommendation") val recommendation: String,
    @SerializedName("completion_percentage") val completionPercentage: Int? = 80
)

// --- Extracted Resume NLP DTOs ---
data class ExtractedSocialProfilesDto(
    @SerializedName("github") val github: String? = null,
    @SerializedName("linkedin") val linkedin: String? = null,
    @SerializedName("portfolio") val portfolio: String? = null
)

data class ExtractedPersonalInfoDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("social_profiles") val socialProfiles: ExtractedSocialProfilesDto = ExtractedSocialProfilesDto()
)

data class ExtractedEducationItemDto(
    @SerializedName("institution") val institution: String? = null,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("field") val field: String? = null,
    @SerializedName("start_year") val startYear: Int? = null,
    @SerializedName("end_year") val endYear: Int? = null,
    @SerializedName("grade") val grade: String? = null
)

data class ExtractedSkillItemDto(
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String = "Technical"
)

data class ExtractedExperienceItemDto(
    @SerializedName("company") val company: String,
    @SerializedName("role") val role: String,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("is_current") val isCurrent: Boolean = false,
    @SerializedName("description") val description: String? = null
)

data class ExtractedProjectItemDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("technologies") val technologies: List<String> = emptyList(),
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("github_url") val githubUrl: String? = null,
    @SerializedName("live_url") val liveUrl: String? = null
)

data class ExtractedCertificationItemDto(
    @SerializedName("name") val name: String,
    @SerializedName("issuer") val issuer: String,
    @SerializedName("date") val date: String? = null
)

data class ExtractedResumeDataDto(
    @SerializedName("personal_info") val personalInfo: ExtractedPersonalInfoDto = ExtractedPersonalInfoDto(),
    @SerializedName("education") val education: List<ExtractedEducationItemDto> = emptyList(),
    @SerializedName("skills") val skills: List<ExtractedSkillItemDto> = emptyList(),
    @SerializedName("experience") val experience: List<ExtractedExperienceItemDto> = emptyList(),
    @SerializedName("projects") val projects: List<ExtractedProjectItemDto> = emptyList(),
    @SerializedName("certifications") val certifications: List<ExtractedCertificationItemDto> = emptyList()
)

data class ResumeAuditReportDto(
    @SerializedName("missing_fields") val missingFields: List<String> = emptyList(),
    @SerializedName("uncertain_fields") val uncertainFields: List<String> = emptyList(),
    @SerializedName("completion_percentage") val completionPercentage: Int = 75,
    @SerializedName("recommendation") val recommendation: String = ""
)

data class ExtractedResumeResponseDto(
    @SerializedName("resume_id") val resumeId: String,
    @SerializedName("title") val title: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("extraction_status") val extractionStatus: String,
    @SerializedName("structured_data") val structuredData: ExtractedResumeDataDto,
    @SerializedName("audit") val audit: ResumeAuditReportDto
)

data class ConfirmResumeResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("profile_id") val profileId: String,
    @SerializedName("profile_strength") val profileStrength: Int,
    @SerializedName("confirmed_items_count") val confirmedItemsCount: Map<String, Int> = emptyMap()
)

// --- Health DTO ---
data class HealthDto(
    @SerializedName("status") val status: String,
    @SerializedName("database") val database: String,
    @SerializedName("environment") val environment: String,
    @SerializedName("version") val version: String
)

// --- AI Assistant DTOs ---
data class AIMessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("sender") val sender: String,
    @SerializedName("content") val content: String,
    @SerializedName("metadata_json") val metadataJson: Map<String, Any>? = null,
    @SerializedName("created_at") val createdAt: String
)

data class AIConversationDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("context_type") val contextType: String,
    @SerializedName("context_id") val contextId: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("messages") val messages: List<AIMessageDto> = emptyList()
)

data class ChatRequestDto(
    @SerializedName("message") val message: String,
    @SerializedName("conversation_id") val conversationId: String? = null,
    @SerializedName("job_id") val jobId: String? = null,
    @SerializedName("context_type") val contextType: String? = "GENERAL"
)

data class ChatResponseDto(
    @SerializedName("conversation_id") val conversationId: String,
    @SerializedName("message_id") val messageId: String,
    @SerializedName("reply") val reply: String,
    @SerializedName("model") val model: String,
    @SerializedName("is_fallback") val isFallback: Boolean,
    @SerializedName("tokens_used") val tokensUsed: Int? = null
)

// --- Resume Builder DTOs ---
data class SavedResumeCreateDto(
    @SerializedName("title") val title: String = "Software Engineer Resume",
    @SerializedName("template_type") val templateType: String = "Modern",
    @SerializedName("target_job_id") val targetJobId: String? = null
)

data class SavedResumeUpdateDto(
    @SerializedName("title") val title: String? = null,
    @SerializedName("template_type") val templateType: String? = null,
    @SerializedName("summary_text") val summaryText: String? = null,
    @SerializedName("contact_json") val contactJson: Map<String, Any>? = null,
    @SerializedName("skills_json") val skillsJson: List<Any>? = null,
    @SerializedName("experience_json") val experienceJson: List<Any>? = null,
    @SerializedName("projects_json") val projectsJson: List<Any>? = null,
    @SerializedName("education_json") val educationJson: List<Any>? = null,
    @SerializedName("certifications_json") val certificationsJson: List<Any>? = null
)

data class SavedResumeResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("template_type") val templateType: String,
    @SerializedName("target_job_id") val targetJobId: String? = null,
    @SerializedName("contact_json") val contactJson: Map<String, Any>? = null,
    @SerializedName("summary_text") val summaryText: String? = null,
    @SerializedName("skills_json") val skillsJson: List<Any>? = null,
    @SerializedName("experience_json") val experienceJson: List<Any>? = null,
    @SerializedName("projects_json") val projectsJson: List<Any>? = null,
    @SerializedName("education_json") val educationJson: List<Any>? = null,
    @SerializedName("certifications_json") val certificationsJson: List<Any>? = null,
    @SerializedName("is_tailored") val isTailored: Boolean = false,
    @SerializedName("tailored_role_title") val tailoredRoleTitle: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class ResumeTailorRequestDto(
    @SerializedName("target_job_id") val targetJobId: String? = null,
    @SerializedName("target_job_title") val targetJobTitle: String? = null,
    @SerializedName("raw_job_description") val rawJobDescription: String? = null
)

data class ResumeTailorResponseDto(
    @SerializedName("saved_resume_id") val savedResumeId: String,
    @SerializedName("tailored_summary") val tailoredSummary: String,
    @SerializedName("matched_keywords_to_emphasize") val matchedKeywordsToEmphasize: List<String>,
    @SerializedName("missing_skills_notice") val missingSkillsNotice: List<String>,
    @SerializedName("is_fallback") val isFallback: Boolean
)

// --- Career Tools DTOs ---
data class CoverLetterRequestDto(
    @SerializedName("job_id") val jobId: String? = null,
    @SerializedName("company") val company: String,
    @SerializedName("role") val role: String,
    @SerializedName("tone") val tone: String = "Professional"
)

data class CoverLetterResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("company") val company: String,
    @SerializedName("role") val role: String,
    @SerializedName("tone") val tone: String,
    @SerializedName("content") val content: String,
    @SerializedName("is_fallback") val isFallback: Boolean = false,
    @SerializedName("created_at") val createdAt: String
)

data class RecruiterMessageRequestDto(
    @SerializedName("job_id") val jobId: String? = null,
    @SerializedName("company") val company: String,
    @SerializedName("role") val role: String,
    @SerializedName("recipient_name") val recipientName: String? = "Hiring Manager",
    @SerializedName("platform") val platform: String = "LinkedIn",
    @SerializedName("context") val context: String? = null
)

data class RecruiterMessageResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("recipient_name") val recipientName: String? = null,
    @SerializedName("platform") val platform: String,
    @SerializedName("content") val content: String,
    @SerializedName("is_fallback") val isFallback: Boolean = false,
    @SerializedName("created_at") val createdAt: String
)

data class JDAnalysisRequestDto(
    @SerializedName("raw_jd_text") val rawJdText: String
)

data class JDAnalysisResponseDto(
    @SerializedName("title") val title: String,
    @SerializedName("company") val company: String,
    @SerializedName("location") val location: String,
    @SerializedName("work_mode") val workMode: String,
    @SerializedName("min_experience_years") val minExperienceYears: Int? = null,
    @SerializedName("education_degree") val educationDegree: String? = null,
    @SerializedName("required_skills") val requiredSkills: List<String>,
    @SerializedName("preferred_skills") val preferredSkills: List<String>,
    @SerializedName("responsibilities") val responsibilities: List<String>,
    @SerializedName("salary_range") val salaryRange: String? = null,
    @SerializedName("match_score") val matchScore: Int,
    @SerializedName("match_tier") val matchTier: String,
    @SerializedName("matched_skills") val matchedSkills: List<String>,
    @SerializedName("missing_skills") val missingSkills: List<String>,
    @SerializedName("explanation") val explanation: String
)

// --- Integrations & Notifications DTOs ---
data class ConnectedAccountDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("account_email") val accountEmail: String? = null,
    @SerializedName("account_name") val accountName: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("is_connected") val isConnected: Boolean,
    @SerializedName("scopes") val scopes: String? = null,
    @SerializedName("last_synced_at") val lastSyncedAt: String? = null,
    @SerializedName("created_at") val createdAt: String
)

data class ProviderStatusDto(
    @SerializedName("provider") val provider: String,
    @SerializedName("is_configured") val isConfigured: Boolean,
    @SerializedName("is_connected") val isConnected: Boolean,
    @SerializedName("account_email") val accountEmail: String? = null,
    @SerializedName("account_name") val accountName: String? = null,
    @SerializedName("last_synced_at") val lastSyncedAt: String? = null,
    @SerializedName("documentation_note") val documentationNote: String
)

data class IntegrationsOverviewDto(
    @SerializedName("google") val google: ProviderStatusDto,
    @SerializedName("github") val github: ProviderStatusDto,
    @SerializedName("linkedin") val linkedin: ProviderStatusDto
)

data class GitHubRepoDto(
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("stargazers_count") val stargazersCount: Int = 0,
    @SerializedName("updated_at") val updatedAt: String? = null
)

data class GitHubImportRequestDto(
    @SerializedName("repo_name") val repoName: String,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("tech_stack") val techStack: String? = null,
    @SerializedName("github_url") val githubUrl: String? = null
)

data class NotificationDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("notification_type") val notificationType: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("deep_link") val deepLink: String? = null,
    @SerializedName("created_at") val createdAt: String
)

