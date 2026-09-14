package com.jobpilot.app.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface JobPilotApiService {
    // --- Health ---
    @GET("health")
    suspend fun checkHealth(): Response<HealthDto>

    // --- Authentication ---
    @POST("auth/login")
    suspend fun login(@Body req: LoginRequestDto): Response<TokenResponseDto>

    @POST("auth/register")
    suspend fun register(@Body req: RegisterRequestDto): Response<UserDto>

    @POST("auth/register/start")
    suspend fun registerStart(@Body req: RegisterStartRequestDto): Response<RegisterStartResponseDto>

    @POST("auth/register/verify")
    suspend fun registerVerify(@Body req: RegisterVerifyRequestDto): Response<TokenResponseDto>

    @GET("auth/me")
    suspend fun getMe(): Response<UserDto>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body req: TokenRefreshRequestDto): Response<TokenResponseDto>

    // --- Profile ---
    @GET("profile")
    suspend fun getProfile(): Response<CareerProfileDto>

    @POST("profile/education")
    suspend fun addEducation(@Body req: EducationDto): Response<EducationDto>

    @DELETE("profile/education/{id}")
    suspend fun deleteEducation(@Path("id") id: String): Response<Unit>

    @POST("profile/skills")
    suspend fun addSkill(@Body req: SkillDto): Response<SkillDto>

    @DELETE("profile/skills/{id}")
    suspend fun deleteSkill(@Path("id") id: String): Response<Unit>

    @POST("profile/projects")
    suspend fun addProject(@Body req: ProjectDto): Response<ProjectDto>

    @DELETE("profile/projects/{id}")
    suspend fun deleteProject(@Path("id") id: String): Response<Unit>

    // --- Jobs ---
    @GET("jobs")
    suspend fun getJobs(
        @Query("query") query: String? = null,
        @Query("work_mode") workMode: String? = null,
        @Query("role") role: String? = null
    ): Response<List<JobDto>>

    @GET("jobs/{id}")
    suspend fun getJobById(@Path("id") id: String): Response<JobDto>

    @GET("jobs/{id}/match")
    suspend fun getJobMatch(@Path("id") id: String): Response<JobMatchDto>

    @GET("jobs/{id}/skill-gaps")
    suspend fun getJobSkillGaps(@Path("id") id: String): Response<SkillGapDto>

    // --- Applications ---
    @GET("applications")
    suspend fun getApplications(
        @Query("status_filter") statusFilter: String? = null
    ): Response<List<ApplicationDto>>

    @POST("applications")
    suspend fun createApplication(@Body req: ApplicationDto): Response<ApplicationDto>

    @GET("applications/{id}")
    suspend fun getApplicationById(@Path("id") id: String): Response<ApplicationDto>

    @PUT("applications/{id}")
    suspend fun updateApplication(
        @Path("id") id: String,
        @Body req: ApplicationDto
    ): Response<ApplicationDto>

    @DELETE("applications/{id}")
    suspend fun deleteApplication(@Path("id") id: String): Response<Unit>

    // --- Resumes & Audits ---
    @Multipart
    @POST("resumes/upload")
    suspend fun uploadResume(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody? = null
    ): Response<ResumeResponseDto>

    @GET("resumes/audit/missing-fields")
    suspend fun auditMissingFields(): Response<MissingFieldsAuditDto>

    @GET("resumes/{id}/extracted-data")
    suspend fun getExtractedResumeData(@Path("id") id: String): Response<ExtractedResumeResponseDto>

    @PUT("resumes/{id}/extracted-data")
    suspend fun updateExtractedResumeData(
        @Path("id") id: String,
        @Body req: ExtractedResumeDataDto
    ): Response<ExtractedResumeResponseDto>

    @POST("resumes/{id}/confirm")
    suspend fun confirmResume(@Path("id") id: String): Response<ConfirmResumeResponseDto>

    // --- AI Assistant ---
    @GET("assistant/conversations")
    suspend fun getConversations(): Response<List<AIConversationDto>>

    @GET("assistant/conversations/{id}")
    suspend fun getConversation(@Path("id") id: String): Response<AIConversationDto>

    @POST("assistant/chat")
    suspend fun chat(@Body req: ChatRequestDto): Response<ChatResponseDto>

    @POST("assistant/quick-coach")
    suspend fun quickCoach(@Body req: ChatRequestDto): Response<ChatResponseDto>

    // --- AI Resume Builder ---
    @POST("resumes/builder/generate")
    suspend fun generateResume(@Body req: SavedResumeCreateDto): Response<SavedResumeResponseDto>

    @GET("resumes/builder/saved")
    suspend fun getSavedResumes(): Response<List<SavedResumeResponseDto>>

    @GET("resumes/builder/{id}")
    suspend fun getSavedResume(@Path("id") id: String): Response<SavedResumeResponseDto>

    @PUT("resumes/builder/{id}")
    suspend fun updateSavedResume(
        @Path("id") id: String,
        @Body req: SavedResumeUpdateDto
    ): Response<SavedResumeResponseDto>

    @POST("resumes/builder/{id}/tailor")
    suspend fun tailorSavedResume(
        @Path("id") id: String,
        @Body req: ResumeTailorRequestDto
    ): Response<ResumeTailorResponseDto>

    @Streaming
    @GET("resumes/builder/{id}/export-pdf")
    suspend fun exportResumePdf(@Path("id") id: String): Response<okhttp3.ResponseBody>

    // --- Career Tools ---
    @POST("career-tools/cover-letter")
    suspend fun generateCoverLetter(@Body req: CoverLetterRequestDto): Response<CoverLetterResponseDto>

    @GET("career-tools/cover-letters")
    suspend fun getCoverLetters(): Response<List<CoverLetterResponseDto>>

    @POST("career-tools/recruiter-message")
    suspend fun generateRecruiterMessage(@Body req: RecruiterMessageRequestDto): Response<RecruiterMessageResponseDto>

    @GET("career-tools/recruiter-messages")
    suspend fun getRecruiterMessages(): Response<List<RecruiterMessageResponseDto>>

    @POST("career-tools/analyze-jd")
    suspend fun analyzeJD(@Body req: JDAnalysisRequestDto): Response<JDAnalysisResponseDto>

    // --- Integrations ---
    @GET("integrations/status")
    suspend fun getIntegrationsStatus(): Response<IntegrationsOverviewDto>

    @GET("integrations/google/connect")
    suspend fun getGoogleConnectUrl(): Response<Map<String, String>>

    @GET("integrations/github/connect")
    suspend fun getGitHubConnectUrl(): Response<Map<String, String>>

    @GET("integrations/linkedin/connect")
    suspend fun getLinkedInConnectUrl(): Response<Map<String, String>>

    @POST("integrations/google/sync")
    suspend fun syncGoogle(): Response<Map<String, Any>>

    @GET("integrations/github/repos")
    suspend fun getGitHubRepos(): Response<List<GitHubRepoDto>>

    @POST("integrations/github/import-project")
    suspend fun importGitHubRepo(@Body req: GitHubImportRequestDto): Response<ProjectDto>

    @POST("integrations/disconnect/{provider}")
    suspend fun disconnectIntegration(@Path("provider") provider: String): Response<Unit>

    // --- Notifications ---
    @GET("notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @PUT("notifications/{id}/read")
    suspend fun markNotificationRead(@Path("id") id: String): Response<NotificationDto>

    @PUT("notifications/mark-all-read")
    suspend fun markAllNotificationsRead(): Response<Unit>
}

