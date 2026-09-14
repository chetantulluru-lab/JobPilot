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
    @SerializedName("full_name") val fullName: String,
    @SerializedName("keystone") val keystone: String = "jobpilot"
)

data class ResetPasswordKeystoneDto(
    @SerializedName("email") val email: String,
    @SerializedName("keystone") val keystone: String,
    @SerializedName("new_password") val newPassword: String
)

data class MessageResponseDto(
    @SerializedName("message") val message: String = ""
)

data class TokenResponseDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
)

data class TokenRefreshRequestDto(
    @SerializedName("refresh_token") val refreshToken: String
)

data class RegisterStartRequestDto(
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterStartResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("email") val email: String
)

data class RegisterVerifyRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String
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

data class PersonalInfoDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("current_role") val currentRole: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("college") val college: String? = null,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("branch") val branch: String? = null
)

data class CareerProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("headline") val headline: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("profile_strength") val profileStrength: Int = 0,
    @SerializedName("current_streak") val currentStreak: Int = 0,
    @SerializedName("longest_streak") val longestStreak: Int = 0,
    @SerializedName("last_activity_date") val lastActivityDate: String? = null,
    @SerializedName("personal_info") val personalInfo: PersonalInfoDto? = null,
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
    @SerializedName("education_requirement") val educationRequirement: String? = null,
    @SerializedName("source") val source: String? = "Direct",
    @SerializedName("source_url") val sourceUrl: String? = null,
    @SerializedName("application_url") val applicationUrl: String? = null,
    @SerializedName("external_id") val externalId: String? = null
)

data class JobMatchDto(
    @SerializedName("job_id") val jobId: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("company") val company: String,
    @SerializedName("match_score") val matchScore: Int? = null,
    @SerializedName("match_tier") val matchTier: String? = null,
    @SerializedName("matched_skills") val matchedSkills: List<String> = emptyList(),
    @SerializedName("missing_skills") val missingSkills: List<String> = emptyList(),
    @SerializedName("explanation") val explanation: String = "",
    @SerializedName("strong_matches") val strongMatches: List<String> = emptyList(),
    @SerializedName("missing_required_skills") val missingRequiredSkills: List<String> = emptyList(),
    @SerializedName("missing_preferred_skills") val missingPreferredSkills: List<String> = emptyList(),
    @SerializedName("weak_skills") val weakSkills: List<String> = emptyList(),
    @SerializedName("experience_relevance") val experienceRelevance: Int = 0,
    @SerializedName("education_relevance") val educationRelevance: Int = 0,
    @SerializedName("profile_completeness") val profileCompleteness: Int = 0,
    @SerializedName("skill_gap_count") val skillGapCount: Int = 0,
    @SerializedName("is_profile_insufficient") val isProfileInsufficient: Boolean = false
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
    @SerializedName("match_score") val matchScore: Int? = null,
    @SerializedName("gaps") val gaps: List<SkillGapItemDto> = emptyList(),
    @SerializedName("is_profile_insufficient") val isProfileInsufficient: Boolean = false
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

data class ResumeResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("file_size_bytes") val fileSizeBytes: Int,
    @SerializedName("missing_fields") val missingFields: List<String> = emptyList(),
    @SerializedName("extraction_status") val extractionStatus: String? = null,
    @SerializedName("completion_percentage") val completionPercentage: Int? = null,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("created_at") val createdAt: String? = null
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

// --- Forgot Password DTOs ---
data class ForgotPasswordStartRequestDto(
    @SerializedName("email") val email: String
)

data class ForgotPasswordStartResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("email") val email: String
)

data class ForgotPasswordVerifyRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("new_password") val newPassword: String
)

data class ForgotPasswordVerifyResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)

// --- Personal Info Update DTO ---
data class PersonalInfoUpdateRequestDto(
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("college") val college: String? = null,
    @SerializedName("degree") val degree: String? = null,
    @SerializedName("branch") val branch: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("bio") val bio: String? = null
)

// --- ATS Resume Analysis DTOs ---
data class ResumeAnalysisResponseDto(
    @SerializedName("resume_id") val resumeId: String,
    @SerializedName("ats_score") val atsScore: Int,
    @SerializedName("label") val label: String = "AI-Powered ATS-Style Analysis",
    @SerializedName("summary") val summary: String,
    @SerializedName("strengths") val strengths: List<String> = emptyList(),
    @SerializedName("weaknesses") val weaknesses: List<String> = emptyList(),
    @SerializedName("missing_skills") val missingSkills: List<String> = emptyList(),
    @SerializedName("content_improvements") val contentImprovements: List<String> = emptyList(),
    @SerializedName("formatting_notes") val formattingNotes: List<String> = emptyList(),
    @SerializedName("disclaimer") val disclaimer: String = "Informational guidance based on industry standards. JobPilot makes no employment or interview guarantees."
)

// --- Roadmap DTOs ---
data class RoadmapSuggestionResponseDto(
    @SerializedName("query") val query: String,
    @SerializedName("suggestions") val suggestions: List<String> = emptyList()
)

data class RoadmapGenerateRequestDto(
    @SerializedName("goal") val goal: String,
    @SerializedName("duration") val duration: String = "6 Months"
)

data class CourseCatalogItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("badge") val badge: String = "Popular",
    @SerializedName("description") val description: String,
    @SerializedName("skills") val skills: List<String> = emptyList(),
    @SerializedName("total_days") val totalDays: Int = 10,
    @SerializedName("total_phases") val totalPhases: Int = 2
)

data class CourseCatalogResponseDto(
    @SerializedName("courses") val courses: List<CourseCatalogItemDto> = emptyList()
)

data class RoadmapGenerateFromCoursesRequestDto(
    @SerializedName("course_ids") val courseIds: List<String>,
    @SerializedName("duration") val duration: String = "6 Months"
)

data class CurriculumAssistantRequestDto(
    @SerializedName("topic") val topic: String,
    @SerializedName("question") val question: String,
    @SerializedName("day_number") val dayNumber: Int? = null
)

data class CurriculumAssistantResponseDto(
    @SerializedName("answer") val answer: String,
    @SerializedName("topic") val topic: String
)

data class PracticeTaskDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("expected_output") val expectedOutput: String? = null
)

data class RoadmapResourceResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("day_id") val dayId: String? = null,
    @SerializedName("phase_id") val phaseId: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("language") val language: String,
    @SerializedName("resource_type") val resourceType: String,
    @SerializedName("source") val source: String
)

data class RoadmapDayResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("roadmap_id") val roadmapId: String,
    @SerializedName("phase_id") val phaseId: String,
    @SerializedName("day_number") val dayNumber: Int,
    @SerializedName("topic") val topic: String,
    @SerializedName("learning_objective") val learningObjective: String? = null,
    @SerializedName("subtopics") val subtopics: List<String> = emptyList(),
    @SerializedName("practice_tasks") val practiceTasks: List<PracticeTaskDto> = emptyList(),
    @SerializedName("is_completed") val isCompleted: Boolean = false,
    @SerializedName("completed_at") val completedAt: String? = null
)

data class RoadmapPhaseResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("roadmap_id") val roadmapId: String,
    @SerializedName("phase_number") val phaseNumber: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("is_unlocked") val isUnlocked: Boolean = false,
    @SerializedName("is_completed") val isCompleted: Boolean = false,
    @SerializedName("project_title") val projectTitle: String? = null,
    @SerializedName("project_description") val projectDescription: String? = null,
    @SerializedName("days") val days: List<RoadmapDayResponseDto> = emptyList(),
    @SerializedName("resources") val resources: List<RoadmapResourceResponseDto> = emptyList()
)

data class RoadmapDetailResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("goal") val goal: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("total_days") val totalDays: Int,
    @SerializedName("completed_days") val completedDays: Int,
    @SerializedName("progress_percentage") val progressPercentage: Int,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("skills_learned") val skillsLearned: List<String> = emptyList(),
    @SerializedName("phases") val phases: List<RoadmapPhaseResponseDto> = emptyList(),
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class RoadmapSummaryResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("goal") val goal: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("total_days") val totalDays: Int,
    @SerializedName("completed_days") val completedDays: Int,
    @SerializedName("progress_percentage") val progressPercentage: Int,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("current_phase_title") val currentPhaseTitle: String? = null,
    @SerializedName("current_day_topic") val currentDayTopic: String? = null,
    @SerializedName("created_at") val createdAt: String
)

data class DayCompleteResponseDto(
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("progress_percentage") val progressPercentage: Int,
    @SerializedName("completed_days") val completedDays: Int,
    @SerializedName("total_days") val totalDays: Int,
    @SerializedName("phase_unlocked") val phaseUnlocked: Boolean,
    @SerializedName("roadmap_completed") val roadmapCompleted: Boolean,
    @SerializedName("current_streak") val currentStreak: Int,
    @SerializedName("skills_learned") val skillsLearned: List<String> = emptyList()
)

data class AddSkillsToResumeResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("added_skills") val addedSkills: List<String> = emptyList()
)


