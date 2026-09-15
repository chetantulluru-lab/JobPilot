package com.jobpilot.app.data.repository

import android.util.Log
import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

private const val TAG = "JobPilotNetwork"


/**
 * NetworkAuthRepository handles user authentication against the FastAPI backend
 * and gracefully falls back to mock authentication if the backend is unreachable.
 */
class NetworkAuthRepository(
    private val apiService: JobPilotApiService,
    private val tokenManager: TokenManager,
    private val mockFallback: MockAuthRepository = MockAuthRepository()
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUserStream: Flow<User?> = _currentUser.asStateFlow()

    init {
        // If a token exists locally, restore or refresh user details from the backend
        if (tokenManager.hasToken()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.getMe()
                    if (response.isSuccessful && response.body() != null) {
                        val dto = response.body()!!
                        _currentUser.value = User(
                            id = dto.id,
                            fullName = dto.fullName,
                            email = dto.email,
                            profileStrength = 0
                        )
                    } else if (response.code() == 401) {
                        // Access token expired, attempt refresh
                        val refreshToken = tokenManager.getRefreshToken()
                        if (!refreshToken.isNullOrBlank()) {
                            val refreshResponse = apiService.refreshToken(TokenRefreshRequestDto(refreshToken))
                            if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                                val tokenDto = refreshResponse.body()!!
                                tokenManager.saveTokens(tokenDto.accessToken, tokenDto.refreshToken)
                                val retryMe = apiService.getMe()
                                if (retryMe.isSuccessful && retryMe.body() != null) {
                                    val dto = retryMe.body()!!
                                    _currentUser.value = User(
                                        id = dto.id,
                                        fullName = dto.fullName,
                                        email = dto.email,
                                        profileStrength = 0
                                    )
                                }
                            } else {
                                tokenManager.clearTokens()
                                _currentUser.value = null
                            }
                        } else {
                            tokenManager.clearTokens()
                            _currentUser.value = null
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Offline or server unreachable, session check deferred: ${e.message}")
                }
            }
        }
    }

    override fun getCurrentUser(): User? = _currentUser.value

    override fun hasActiveSession(): Boolean {
        return tokenManager.hasToken()
    }

    override fun isOnboardingCompleted(): Boolean {
        return tokenManager.isOnboardingCompleted()
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        tokenManager.setOnboardingCompleted(completed)
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = apiService.login(LoginRequestDto(email = email, password = password))
            if (response.isSuccessful && response.body() != null) {
                val tokenDto = response.body()!!
                tokenManager.saveTokens(tokenDto.accessToken, tokenDto.refreshToken)

                // Fetch real user profile
                val meResponse = apiService.getMe()
                val user = if (meResponse.isSuccessful && meResponse.body() != null) {
                    val meDto = meResponse.body()!!
                    User(
                        id = meDto.id,
                        fullName = meDto.fullName,
                        email = meDto.email,
                        profileStrength = 0
                    )
                } else {
                    User(
                        id = "user-real",
                        fullName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                        email = email,
                        profileStrength = 0
                    )
                }
                _currentUser.value = user
                Result.success(user)
            } else if (response.code() == 401 || response.code() == 400) {
                Result.failure(IllegalArgumentException("Invalid email or password"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login failed (${response.code()})"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "Network error during login: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server. Please check your internet connection."))
        }
    }

    override suspend fun register(fullName: String, email: String, password: String, keystone: String): Result<User> {
        return try {
            val response = apiService.register(
                RegisterRequestDto(fullName = fullName, email = email, password = password, keystone = keystone)
            )
            if (response.isSuccessful && response.body() != null) {
                // Automatically log in after registration
                login(email, password)
            } else if (response.code() == 400 || response.code() == 409) {
                Result.failure(IllegalArgumentException("An account with this email already exists"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Registration failed (${response.code()})"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "Network error during register: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server. Please check your internet connection."))
        }
    }

    override suspend fun resetPasswordWithKeystone(email: String, keystone: String, newPassword: String): Result<Unit> {
        return try {
            val response = apiService.resetPasswordWithKeystone(
                ResetPasswordKeystoneDto(email = email, keystone = keystone, newPassword = newPassword)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to reset password"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "resetPasswordWithKeystone error: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server. Please check your internet connection."))
        }
    }

    override suspend fun startRegistration(fullName: String, email: String, password: String): Result<String> {
        return try {
            val response = apiService.registerStart(
                RegisterStartRequestDto(fullName = fullName, email = email, password = password)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to send verification code"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "startRegistration network error: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server."))
        }
    }

    override suspend fun verifyRegistrationOtp(email: String, otp: String): Result<User> {
        return try {
            val response = apiService.registerVerify(
                RegisterVerifyRequestDto(email = email, otp = otp)
            )
            if (response.isSuccessful && response.body() != null) {
                val tokenDto = response.body()!!
                tokenManager.saveTokens(tokenDto.accessToken, tokenDto.refreshToken)
                val meResponse = apiService.getMe()
                val user = if (meResponse.isSuccessful && meResponse.body() != null) {
                    val meDto = meResponse.body()!!
                    User(
                        id = meDto.id,
                        fullName = meDto.fullName,
                        email = meDto.email,
                        profileStrength = 0
                    )
                } else {
                    User(
                        id = "user-real",
                        fullName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                        email = email,
                        profileStrength = 0
                    )
                }
                _currentUser.value = user
                Result.success(user)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Invalid verification code"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "verifyRegistrationOtp network error: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server."))
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return mockFallback.sendPasswordReset(email)
    }

    override suspend fun startForgotPassword(email: String): Result<String> {
        return try {
            val response = apiService.forgotPasswordStart(
                ForgotPasswordStartRequestDto(email = email)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to send reset code"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "startForgotPassword network error: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server."))
        }
    }

    override suspend fun verifyForgotPassword(email: String, otp: String, newPassword: String): Result<Unit> {
        return try {
            val response = apiService.forgotPasswordVerify(
                ForgotPasswordVerifyRequestDto(email = email, otp = otp, newPassword = newPassword)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to reset password"
                Result.failure(IllegalArgumentException(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "verifyForgotPassword network error: ${e.message}")
            Result.failure(java.io.IOException("Unable to connect to server."))
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
        _currentUser.value = null
    }
}

/**
 * NetworkJobRepository fetches live jobs and AI match explanations from FastAPI.
 */
class NetworkJobRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockJobRepository = MockJobRepository()
) : JobRepository {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    override val jobsStream: Flow<List<Job>> = _jobs.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            refreshJobs()
        }
    }

    private fun mapDtoToJob(dto: JobDto): Job {
        return Job(
            id = dto.id,
            title = dto.title,
            company = dto.company,
            companyLogoUrl = null,
            location = dto.location,
            workMode = dto.workMode,
            employmentType = dto.employmentType,
            stipendOrSalary = dto.salaryRange ?: "Competitive",
            description = dto.description,
            postedDaysAgo = 1,
            requirements = dto.requirements?.split("\n")?.map { it.trim() }?.filter { it.isNotBlank() }
                ?: dto.skillsRequired.split(",").map { it.trim() }.filter { it.isNotBlank() },
            matchDetails = JobMatch(
                matchScore = null,
                matchTier = null,
                strongMatches = emptyList(),
                missingSkills = emptyList(),
                whyItMatchesExplanation = "",
                isProfileInsufficient = true
            ),
            source = dto.source ?: "Direct",
            applicationUrl = dto.applicationUrl
        )
    }

    override suspend fun refreshJobs(query: String?, workMode: String?): List<Job> {
        return try {
            val q = query?.takeIf { it.isNotBlank() }
            val mode = workMode?.takeIf { it != "All" && it.isNotBlank() }
            val response = apiService.getJobs(query = q, workMode = mode)
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()!!
                val domainJobs = dtoList.map { dto -> mapDtoToJob(dto) }
                _jobs.value = domainJobs
                domainJobs
            } else {
                _jobs.value
            }
        } catch (e: Exception) {
            Log.w(TAG, "Backend unreachable for jobs: ${e.message}")
            _jobs.value
        }
    }

    override fun getAllJobs(): List<Job> = _jobs.value

    override fun getJobById(id: String): Job? = _jobs.value.find { it.id == id }

    override suspend fun filterJobs(query: String, workMode: String?): List<Job> {
        val serverResults = refreshJobs(query, workMode)
        if (serverResults.isNotEmpty()) return serverResults
        return _jobs.value.filter { job ->
            val matchesQuery = query.isBlank() ||
                job.title.contains(query, ignoreCase = true) ||
                job.company.contains(query, ignoreCase = true) ||
                job.requirements.any { it.contains(query, ignoreCase = true) }
            val matchesMode = workMode == null || workMode == "All" || job.workMode.equals(workMode, ignoreCase = true)
            matchesQuery && matchesMode
        }
    }

    override suspend fun getJobMatch(jobId: String): JobMatch? {
        return try {
            val res = apiService.getJobMatch(jobId)
            if (res.isSuccessful && res.body() != null) {
                val dto = res.body()!!
                val tier = dto.matchTier?.let { t ->
                    when {
                        t.contains("EXCEPTIONAL", ignoreCase = true) -> MatchTier.EXCEPTIONAL
                        t.contains("STRONG", ignoreCase = true) -> MatchTier.STRONG
                        t.contains("MODERATE", ignoreCase = true) -> MatchTier.MODERATE
                        t.contains("DEVELOPING", ignoreCase = true) -> MatchTier.DEVELOPING
                        else -> null
                    }
                }
                val match = JobMatch(
                    matchScore = dto.matchScore,
                    matchTier = tier,
                    strongMatches = dto.strongMatches.ifEmpty { dto.matchedSkills },
                    missingSkills = dto.missingSkills,
                    whyItMatchesExplanation = dto.explanation,
                    isProfileInsufficient = dto.isProfileInsufficient || dto.matchScore == null
                )
                _jobs.update { list ->
                    list.map { if (it.id == jobId) it.copy(matchDetails = match) else it }
                }
                match
            } else null
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get job match for $jobId: ${e.message}")
            null
        }
    }
}

/**
 * NetworkProfileRepository synchronizes career profile details with FastAPI.
 */
class NetworkProfileRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockProfileRepository = MockProfileRepository()
) : ProfileRepository {

    private val _profile = MutableStateFlow(CareerProfile.empty())
    override val profileStream: Flow<CareerProfile> = _profile.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            refreshProfile()
        }
    }

    private fun mapDtoToCareerProfile(dto: CareerProfileDto): CareerProfile {
        val pi = dto.personalInfo
        val personalInfo = PersonalInfo(
            fullName = pi?.fullName ?: "",
            email = pi?.email ?: "",
            phone = pi?.phone ?: "",
            location = pi?.location ?: "",
            professionalSummary = pi?.bio ?: (dto.summary ?: ""),
            age = pi?.age,
            college = pi?.college,
            degree = pi?.degree,
            branch = pi?.branch,
            avatarUrl = pi?.avatarUrl
        )

        val mappedEducation = dto.education.map { edu ->
            Education(
                id = edu.id ?: java.util.UUID.randomUUID().toString(),
                degree = edu.degree,
                college = edu.institution,
                branch = edu.fieldOfStudy ?: "",
                startDate = edu.startYear?.toString() ?: "",
                endDate = edu.endYear?.toString() ?: "",
                grade = edu.gradeOrCgpa ?: ""
            )
        }

        val mappedSkills = dto.skills.map { s ->
            val cat = when (s.category?.uppercase()) {
                "PROGRAMMING_LANGUAGE" -> SkillCategory.PROGRAMMING_LANGUAGE
                "FRAMEWORK" -> SkillCategory.FRAMEWORK
                "DATABASE" -> SkillCategory.DATABASE
                "TOOL" -> SkillCategory.TOOL
                "CLOUD" -> SkillCategory.CLOUD
                else -> SkillCategory.OTHER
            }
            Skill(
                id = s.id ?: java.util.UUID.randomUUID().toString(),
                name = s.name,
                category = cat,
                proficiencyLevel = s.proficiency ?: "Proficient"
            )
        }

        val mappedExperience = dto.experience.map { exp ->
            Experience(
                id = exp.id ?: java.util.UUID.randomUUID().toString(),
                company = exp.company,
                role = exp.title,
                startDate = exp.startDate ?: "",
                endDate = exp.endDate ?: "",
                description = exp.description ?: "",
                technologies = emptyList()
            )
        }

        val mappedProjects = dto.projects.map { p ->
            Project(
                id = p.id ?: java.util.UUID.randomUUID().toString(),
                name = p.title,
                description = p.description ?: "",
                technologies = p.techStack?.split(",")?.map { t -> t.trim() }?.filter { t -> t.isNotEmpty() } ?: emptyList(),
                startDate = "",
                endDate = "",
                githubUrl = p.githubUrl,
                liveUrl = p.liveUrl
            )
        }

        val mappedCertifications = dto.certifications.map { cert ->
            Certification(
                id = cert.id ?: java.util.UUID.randomUUID().toString(),
                name = cert.name,
                issuingOrganization = cert.issuer,
                date = cert.issueDate ?: "",
                credentialUrl = cert.credentialUrl
            )
        }

        val mappedSocial = dto.socialProfiles.map { sp ->
            SocialProfile(
                id = sp.id ?: java.util.UUID.randomUUID().toString(),
                platform = sp.platform,
                url = sp.url
            )
        }

        val firstPref = dto.jobPreferences.firstOrNull()
        val mappedJobPrefs = JobPreference(
            targetRoles = firstPref?.desiredRoles?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
            preferredLocations = firstPref?.preferredLocations?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
            workMode = firstPref?.workModes ?: "Remote",
            employmentType = "Full-time",
            salaryExpectation = if (firstPref?.minExpectedSalary != null) "${firstPref.minExpectedSalary} - ${firstPref.maxExpectedSalary ?: ""}" else "",
            preferredTechnologies = emptyList()
        )

        return CareerProfile(
            id = dto.id,
            userId = dto.userId,
            personalInfo = personalInfo,
            education = mappedEducation,
            skills = mappedSkills,
            experience = mappedExperience,
            projects = mappedProjects,
            certifications = mappedCertifications,
            socialProfiles = mappedSocial,
            jobPreferences = mappedJobPrefs,
            profileStrengthScore = dto.profileStrength,
            currentStreak = dto.currentStreak,
            longestStreak = dto.longestStreak,
            lastActivityDate = dto.lastActivityDate
        )
    }

    override fun getProfile(): CareerProfile = _profile.value

    override suspend fun refreshProfile(): Result<CareerProfile> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val profile = mapDtoToCareerProfile(response.body()!!)
                _profile.value = profile
                Result.success(profile)
            } else {
                Result.success(_profile.value)
            }
        } catch (e: Exception) {
            Log.w(TAG, "refreshProfile error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updatePersonalInfo(personalInfo: PersonalInfo) {
        _profile.update { it.copy(personalInfo = personalInfo) }
        try {
            val res = apiService.updatePersonalInfo(
                PersonalInfoUpdateRequestDto(
                    fullName = personalInfo.fullName,
                    email = personalInfo.email,
                    age = personalInfo.age,
                    college = personalInfo.college,
                    degree = personalInfo.degree,
                    branch = personalInfo.branch,
                    phone = personalInfo.phone,
                    location = personalInfo.location,
                    bio = personalInfo.professionalSummary
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync updatePersonalInfo: ${e.message}")
        }
    }

    override suspend fun uploadProfilePhoto(bytes: ByteArray, filename: String): Result<String> {
        return try {
            val reqFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", filename, reqFile)
            val response = apiService.uploadProfilePhoto(part)
            if (response.isSuccessful && response.body() != null) {
                val avatarUrl = response.body()?.avatarUrl ?: ""
                _profile.update {
                    it.copy(personalInfo = it.personalInfo.copy(avatarUrl = avatarUrl))
                }
                refreshProfile()
                Result.success(avatarUrl)
            } else {
                val error = response.errorBody()?.string() ?: "Failed to upload photo"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadProfilePhoto error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteProfilePhoto(): Result<Unit> {
        return try {
            val response = apiService.deleteProfilePhoto()
            if (response.isSuccessful) {
                _profile.update {
                    it.copy(personalInfo = it.personalInfo.copy(avatarUrl = null))
                }
                refreshProfile()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete photo"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteProfilePhoto error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun addEducation(education: Education) {
        _profile.update { it.copy(education = it.education + education) }
        try {
            val res = apiService.addEducation(
                EducationDto(
                    institution = education.college,
                    degree = education.degree,
                    fieldOfStudy = education.branch,
                    startYear = education.startDate.toIntOrNull(),
                    endYear = education.endDate.toIntOrNull(),
                    gradeOrCgpa = education.grade
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addEducation to backend: ${e.message}")
        }
    }

    override suspend fun updateEducation(education: Education) {
        _profile.update { it.copy(education = it.education.map { edu -> if (edu.id == education.id) education else edu }) }
        try {
            val res = apiService.updateEducation(
                id = education.id,
                req = EducationDto(
                    institution = education.college,
                    degree = education.degree,
                    fieldOfStudy = education.branch,
                    startYear = education.startDate.toIntOrNull(),
                    endYear = education.endDate.toIntOrNull(),
                    gradeOrCgpa = education.grade
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync updateEducation to backend: ${e.message}")
        }
    }

    override suspend fun removeEducation(educationId: String) {
        _profile.update { it.copy(education = it.education.filterNot { edu -> edu.id == educationId }) }
        try {
            val res = apiService.deleteEducation(educationId)
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync deleteEducation to backend: ${e.message}")
        }
    }

    override suspend fun addSkill(skill: Skill) {
        _profile.update { it.copy(skills = it.skills + skill) }
        try {
            val res = apiService.addSkill(
                SkillDto(
                    name = skill.name,
                    category = skill.category.name,
                    proficiency = skill.proficiencyLevel
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addSkill to backend: ${e.message}")
        }
    }

    override suspend fun removeSkill(skillId: String) {
        _profile.update { it.copy(skills = it.skills.filterNot { s -> s.id == skillId }) }
        try {
            val res = apiService.deleteSkill(skillId)
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync deleteSkill to backend: ${e.message}")
        }
    }

    override suspend fun addExperience(experience: Experience) {
        _profile.update { it.copy(experience = it.experience + experience) }
        try {
            val res = apiService.addExperience(
                ExperienceDto(
                    company = experience.company,
                    title = experience.role,
                    startDate = experience.startDate,
                    endDate = experience.endDate,
                    description = experience.description
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addExperience to backend: ${e.message}")
        }
    }

    override suspend fun updateExperience(experience: Experience) {
        _profile.update { it.copy(experience = it.experience.map { exp -> if (exp.id == experience.id) experience else exp }) }
        try {
            val res = apiService.updateExperience(
                id = experience.id,
                req = ExperienceDto(
                    company = experience.company,
                    title = experience.role,
                    startDate = experience.startDate,
                    endDate = experience.endDate,
                    description = experience.description
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync updateExperience to backend: ${e.message}")
        }
    }

    override suspend fun removeExperience(experienceId: String) {
        _profile.update { it.copy(experience = it.experience.filterNot { exp -> exp.id == experienceId }) }
        try {
            val res = apiService.deleteExperience(experienceId)
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync deleteExperience to backend: ${e.message}")
        }
    }

    override suspend fun addProject(project: Project) {
        _profile.update { it.copy(projects = it.projects + project) }
        try {
            val res = apiService.addProject(
                ProjectDto(
                    title = project.name,
                    description = project.description,
                    techStack = project.technologies.joinToString(", "),
                    githubUrl = project.githubUrl,
                    liveUrl = project.liveUrl
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addProject to backend: ${e.message}")
        }
    }

    override suspend fun updateProject(project: Project) {
        _profile.update { it.copy(projects = it.projects.map { p -> if (p.id == project.id) project else p }) }
        try {
            val res = apiService.updateProject(
                id = project.id,
                req = ProjectDto(
                    title = project.name,
                    description = project.description,
                    techStack = project.technologies.joinToString(", "),
                    githubUrl = project.githubUrl,
                    liveUrl = project.liveUrl
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync updateProject to backend: ${e.message}")
        }
    }

    override suspend fun removeProject(projectId: String) {
        _profile.update { it.copy(projects = it.projects.filterNot { p -> p.id == projectId }) }
        try {
            val res = apiService.deleteProject(projectId)
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync deleteProject to backend: ${e.message}")
        }
    }

    override suspend fun addCertification(certification: Certification) {
        _profile.update { it.copy(certifications = it.certifications + certification) }
        try {
            val res = apiService.addCertification(
                CertificationDto(
                    name = certification.name,
                    issuer = certification.issuingOrganization,
                    issueDate = certification.date,
                    credentialUrl = certification.credentialUrl
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addCertification to backend: ${e.message}")
        }
    }

    override suspend fun updateCertification(certification: Certification) {
        _profile.update { it.copy(certifications = it.certifications.map { c -> if (c.id == certification.id) certification else c }) }
        try {
            val res = apiService.updateCertification(
                id = certification.id,
                req = CertificationDto(
                    name = certification.name,
                    issuer = certification.issuingOrganization,
                    issueDate = certification.date,
                    credentialUrl = certification.credentialUrl
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync updateCertification to backend: ${e.message}")
        }
    }

    override suspend fun removeCertification(certificationId: String) {
        _profile.update { it.copy(certifications = it.certifications.filterNot { c -> c.id == certificationId }) }
        try {
            val res = apiService.deleteCertification(certificationId)
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync deleteCertification to backend: ${e.message}")
        }
    }

    override suspend fun updateJobPreferences(preferences: JobPreference) {
        _profile.update { it.copy(jobPreferences = preferences) }
        try {
            val res = apiService.setJobPreferences(
                JobPreferenceDto(
                    desiredRoles = preferences.targetRoles.joinToString(", "),
                    preferredLocations = preferences.preferredLocations.joinToString(", "),
                    workModes = preferences.workMode
                )
            )
            if (res.isSuccessful) {
                refreshProfile()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync preferences to backend: ${e.message}")
        }
    }
}

/**
 * NetworkApplicationRepository manages job application status and pipeline stages.
 */
class NetworkApplicationRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockApplicationRepository = MockApplicationRepository()
) : ApplicationRepository {

    private val _applications = MutableStateFlow<List<JobApplication>>(emptyList())
    override val applicationsStream: Flow<List<JobApplication>> = _applications.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getApplications()
                if (response.isSuccessful && response.body() != null) {
                    val dtoList = response.body()!!
                    _applications.value = dtoList.map { dto ->
                        val status = ApplicationStatus.values().firstOrNull {
                            it.name.equals(dto.status, ignoreCase = true) ||
                                it.displayName.equals(dto.status, ignoreCase = true)
                        } ?: ApplicationStatus.APPLIED

                        val timelineEvents = dto.events.map { ev ->
                            val evStatus = ApplicationStatus.values().firstOrNull {
                                it.name.equals(ev.stage, ignoreCase = true) ||
                                    it.displayName.equals(ev.stage, ignoreCase = true)
                            } ?: ApplicationStatus.APPLIED
                            ApplicationEvent(
                                id = ev.id,
                                status = evStatus,
                                title = ev.title,
                                description = ev.description ?: "",
                                timestamp = ev.eventDate
                            )
                        }

                        JobApplication(
                            id = dto.id,
                            jobId = dto.jobId ?: "job-${dto.id}",
                            jobTitle = dto.role,
                            company = dto.company,
                            location = dto.location ?: "Remote",
                            currentStatus = status,
                            appliedDate = dto.appliedDate,
                            lastUpdated = "Recently",
                            notes = dto.notes ?: "",
                            recruiterContact = dto.recruiterEmail,
                            matchScoreAtApplication = 88,
                            timeline = timelineEvents.ifEmpty {
                                listOf(
                                    ApplicationEvent(
                                        id = "ev-init",
                                        status = status,
                                        title = status.displayName,
                                        description = "Application updated",
                                        timestamp = dto.appliedDate
                                    )
                                )
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Backend unreachable for applications: ${e.message}")
            }
        }
    }

    override fun getAllApplications(): List<JobApplication> = _applications.value

    override fun getApplicationById(id: String): JobApplication? =
        _applications.value.find { it.id == id }

    override suspend fun addApplication(application: JobApplication) {
        _applications.update { listOf(application) + it }
        try {
            apiService.createApplication(
                ApplicationDto(
                    id = application.id,
                    userId = "user-me",
                    jobId = application.jobId,
                    company = application.company,
                    role = application.jobTitle,
                    location = application.location,
                    status = application.currentStatus.name,
                    appliedDate = application.appliedDate,
                    notes = application.notes,
                    recruiterEmail = application.recruiterContact
                )
            )
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync addApplication to backend: ${e.message}")
        }
    }

    override suspend fun updateStatus(id: String, newStatus: ApplicationStatus, eventDescription: String) {
        _applications.update { list ->
            list.map { app ->
                if (app.id == id) {
                    val newEvent = ApplicationEvent(
                        id = "ev-${System.currentTimeMillis()}",
                        status = newStatus,
                        title = newStatus.displayName,
                        description = eventDescription,
                        timestamp = "Just now"
                    )
                    app.copy(
                        currentStatus = newStatus,
                        lastUpdated = "Just now",
                        timeline = app.timeline + newEvent
                    )
                } else app
            }
        }
    }
}

/**
 * NetworkResumeRepository synchronizes resume generation, tailoring, and PDF export with FastAPI.
 */
class NetworkResumeRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockResumeRepository = MockResumeRepository()
) : ResumeRepository {

    private val _resumes = MutableStateFlow<List<Resume>>(emptyList())
    override val resumesStream: Flow<List<Resume>> = _resumes.asStateFlow()

    init {
        refreshResumes()
    }

    private fun refreshResumes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getSavedResumes()
                if (response.isSuccessful && response.body() != null) {
                    val dtoList = response.body()!!
                    val domainResumes = dtoList.map { dto ->
                        val template = ResumeTemplateType.values().firstOrNull {
                            it.templateName.equals(dto.templateType, ignoreCase = true) ||
                                it.name.equals(dto.templateType, ignoreCase = true)
                        } ?: ResumeTemplateType.MODERN
                        Resume(
                            id = dto.id,
                            title = dto.title,
                            templateType = template,
                            lastModified = dto.updatedAt.take(10),
                            profileSnapshot = CareerProfile.empty(),
                            isDefault = false,
                            tailoredForJobTitle = dto.tailoredRoleTitle
                        )
                    }
                    _resumes.value = domainResumes
                }
            } catch (e: Exception) {
                Log.w(TAG, "Backend unreachable for saved resumes: ${e.message}")
            }
        }
    }

    override fun getAllResumes(): List<Resume> = _resumes.value

    override suspend fun parseUploadedResume(fileName: String): ResumeParsedData {
        return mockFallback.parseUploadedResume(fileName)
    }

    override suspend fun uploadResumeFile(bytes: ByteArray, fileName: String, mimeType: String): Result<ResumeParsedData> {
        return try {
            val mediaType = mimeType.toMediaTypeOrNull() ?: "application/pdf".toMediaTypeOrNull()
            val requestBody = bytes.toRequestBody(mediaType)
            val part = MultipartBody.Part.createFormData("file", fileName, requestBody)
            val uploadResponse = apiService.uploadResume(part)
            if (!uploadResponse.isSuccessful || uploadResponse.body() == null) {
                val errorMsg = uploadResponse.errorBody()?.string() ?: "Upload failed (${uploadResponse.code()})"
                return Result.failure(Exception(errorMsg))
            }
            val resumeDto = uploadResponse.body()!!
            val extractedResponse = apiService.getExtractedResumeData(resumeDto.id)
            if (!extractedResponse.isSuccessful || extractedResponse.body() == null) {
                return Result.failure(Exception("Failed to fetch extracted resume data"))
            }
            val extDto = extractedResponse.body()!!
            val data = extDto.structuredData
            val parsed = ResumeParsedData(
                resumeId = extDto.resumeId,
                detectedName = data.personalInfo.name ?: "",
                detectedEmail = data.personalInfo.email ?: "",
                detectedPhone = data.personalInfo.phone ?: "",
                detectedEducation = data.education.map { edu ->
                    Education(
                        id = UUID.randomUUID().toString(),
                        degree = edu.degree ?: "",
                        college = edu.institution ?: "",
                        branch = edu.field ?: "",
                        startDate = edu.startYear?.toString() ?: "",
                        endDate = edu.endYear?.toString() ?: "",
                        grade = edu.grade ?: ""
                    )
                },
                detectedSkills = data.skills.map { s ->
                    val cat = when (s.category.uppercase()) {
                        "PROGRAMMING_LANGUAGE" -> SkillCategory.PROGRAMMING_LANGUAGE
                        "FRAMEWORK" -> SkillCategory.FRAMEWORK
                        "DATABASE" -> SkillCategory.DATABASE
                        "TOOL" -> SkillCategory.TOOL
                        "CLOUD" -> SkillCategory.CLOUD
                        else -> SkillCategory.OTHER
                    }
                    Skill(id = UUID.randomUUID().toString(), name = s.name, category = cat)
                },
                detectedExperience = data.experience.map { exp ->
                    Experience(
                        id = UUID.randomUUID().toString(),
                        company = exp.company,
                        role = exp.role,
                        startDate = exp.startDate ?: "",
                        endDate = exp.endDate ?: "",
                        description = exp.description ?: "",
                        technologies = emptyList()
                    )
                },
                detectedProjects = data.projects.map { p ->
                    Project(
                        id = UUID.randomUUID().toString(),
                        name = p.name,
                        description = p.description ?: "",
                        technologies = p.technologies,
                        startDate = p.startDate ?: "",
                        endDate = p.endDate ?: "",
                        githubUrl = p.githubUrl,
                        liveUrl = p.liveUrl
                    )
                },
                missingFields = extDto.audit.missingFields.map { field ->
                    MissingField(
                        fieldKey = field,
                        fieldLabel = field.replace("_", " ").split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } },
                        reason = "Omitted or not detected in document",
                        suggestedAction = "Add to profile"
                    )
                }
            )
            Result.success(parsed)
        } catch (e: Exception) {
            Log.e(TAG, "uploadResumeFile error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun confirmResume(resumeId: String): Result<Unit> {
        return try {
            val response = apiService.confirmResume(resumeId)
            if (response.isSuccessful) {
                refreshResumes()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to confirm resume: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "confirmResume error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun createResume(
        title: String,
        templateType: ResumeTemplateType,
        profile: CareerProfile
    ): Resume {
        return try {
            val response = apiService.generateResume(
                SavedResumeCreateDto(
                    title = title,
                    templateType = templateType.templateName
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                val newResume = Resume(
                    id = dto.id,
                    title = dto.title,
                    templateType = templateType,
                    lastModified = "Just now",
                    profileSnapshot = profile,
                    isDefault = false
                )
                _resumes.update { listOf(newResume) + it }
                newResume
            } else {
                mockFallback.createResume(title, templateType, profile).also { r ->
                    _resumes.update { listOf(r) + it }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "createResume failed, falling back to mock: ${e.message}")
            mockFallback.createResume(title, templateType, profile).also { r ->
                _resumes.update { listOf(r) + it }
            }
        }
    }

    override suspend fun tailorResumeForJob(resumeId: String, jobTitle: String): Resume {
        return try {
            val response = apiService.tailorSavedResume(
                resumeId,
                ResumeTailorRequestDto(targetJobTitle = jobTitle)
            )
            if (response.isSuccessful && response.body() != null) {
                var tailored: Resume? = null
                _resumes.update { list ->
                    list.map { r ->
                        if (r.id == resumeId) {
                            val updated = r.copy(
                                title = "${r.title} (Tailored for $jobTitle)",
                                lastModified = "Just now",
                                tailoredForJobTitle = jobTitle
                            )
                            tailored = updated
                            updated
                        } else r
                    }
                }
                tailored ?: mockFallback.tailorResumeForJob(resumeId, jobTitle)
            } else {
                mockFallback.tailorResumeForJob(resumeId, jobTitle)
            }
        } catch (e: Exception) {
            Log.w(TAG, "tailorResumeForJob failed, falling back to mock: ${e.message}")
            mockFallback.tailorResumeForJob(resumeId, jobTitle)
        }
    }

    override suspend fun exportPdfToFile(resumeId: String, destFile: File): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.exportResumePdf(resumeId)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    body.byteStream().use { input ->
                        FileOutputStream(destFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Result.success(destFile)
                } else {
                    mockFallback.exportPdfToFile(resumeId, destFile)
                }
            } catch (e: Exception) {
                Log.w(TAG, "exportPdfToFile network failure, falling back: ${e.message}")
                mockFallback.exportPdfToFile(resumeId, destFile)
            }
        }
    }

    override suspend fun analyzeResume(resumeId: String): Result<ResumeAnalysis> {
        return try {
            val response = apiService.analyzeResume(resumeId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    ResumeAnalysis(
                        resumeId = dto.resumeId,
                        atsScore = dto.atsScore,
                        label = dto.label,
                        summary = dto.summary,
                        strengths = dto.strengths,
                        weaknesses = dto.weaknesses,
                        missingSkills = dto.missingSkills,
                        contentImprovements = dto.contentImprovements,
                        formattingNotes = dto.formattingNotes,
                        disclaimer = dto.disclaimer
                    )
                )
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to analyze resume"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "analyzeResume error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getResumeAnalysis(resumeId: String): Result<ResumeAnalysis> {
        return try {
            val response = apiService.getResumeAnalysis(resumeId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    ResumeAnalysis(
                        resumeId = dto.resumeId,
                        atsScore = dto.atsScore,
                        label = dto.label,
                        summary = dto.summary,
                        strengths = dto.strengths,
                        weaknesses = dto.weaknesses,
                        missingSkills = dto.missingSkills,
                        contentImprovements = dto.contentImprovements,
                        formattingNotes = dto.formattingNotes,
                        disclaimer = dto.disclaimer
                    )
                )
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Analysis not found"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getResumeAnalysis error: ${e.message}", e)
            Result.failure(e)
        }
    }
}

/**
 * NetworkNotificationRepository synchronizes in-app notifications with FastAPI.
 */
class NetworkNotificationRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockNotificationRepository = MockNotificationRepository()
) : NotificationRepository {

    private val _notifications = MutableStateFlow(mockFallback.getAllNotifications())
    override val notificationsStream: Flow<List<NotificationItem>> = _notifications.asStateFlow()

    init {
        refreshNotifications()
    }

    private fun refreshNotifications() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getNotifications()
                if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                    val dtoList = response.body()!!
                    val domainList = dtoList.map { dto ->
                        val type = when (dto.notificationType) {
                            "APPLICATION_UPDATE" -> NotificationType.APPLICATION_UPDATE
                            "INTERVIEW_UPDATE" -> NotificationType.INTERVIEW_UPDATE
                            "GMAIL_EVENT" -> NotificationType.GMAIL_EVENT
                            "PROFILE_IMPROVEMENT" -> NotificationType.PROFILE_IMPROVEMENT
                            else -> NotificationType.NEW_JOB_MATCH
                        }
                        NotificationItem(
                            id = dto.id,
                            title = dto.title,
                            message = dto.message,
                            timestamp = dto.createdAt.take(10),
                            type = type,
                            isRead = dto.isRead,
                            relatedId = dto.deepLink
                        )
                    }
                    _notifications.value = domainList
                }
            } catch (e: Exception) {
                Log.w(TAG, "Backend unreachable for notifications: ${e.message}")
            }
        }
    }

    override fun getAllNotifications(): List<NotificationItem> = _notifications.value

    override suspend fun markAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
        try {
            apiService.markNotificationRead(id)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync markAsRead: ${e.message}")
        }
    }

    override suspend fun markAllAsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
        try {
            apiService.markAllNotificationsRead()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync markAllAsRead: ${e.message}")
        }
    }
}

/**
 * NetworkConnectedAccountRepository manages OAuth connections and sync with FastAPI.
 */
class NetworkConnectedAccountRepository(
    private val apiService: JobPilotApiService,
    private val mockFallback: MockConnectedAccountRepository = MockConnectedAccountRepository()
) : ConnectedAccountRepository {

    private val defaultAccounts = listOf(
        ConnectedAccount(
            provider = AccountProvider.GOOGLE_GMAIL,
            isConnected = false,
            syncStatus = "Not Connected",
            note = "Connect securely via Google OAuth 2.0. We never ask for or store passwords."
        ),
        ConnectedAccount(
            provider = AccountProvider.GITHUB,
            isConnected = false,
            syncStatus = "Not Connected",
            note = "Import verified public repositories and open-source contributions."
        ),
        ConnectedAccount(
            provider = AccountProvider.LINKEDIN,
            isConnected = false,
            syncStatus = "Not Connected",
            note = "Import verified professional headline and career history."
        )
    )

    private val _accounts = MutableStateFlow(defaultAccounts)
    private val _emailEvents = MutableStateFlow<List<EmailEvent>>(emptyList())

    override val accountsStream: Flow<List<ConnectedAccount>> = _accounts.asStateFlow()
    override val emailEventsStream: Flow<List<EmailEvent>> = _emailEvents.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            refreshAccounts()
        }
    }

    override suspend fun refreshAccounts() {
        try {
            val response = apiService.getIntegrationsStatus()
            if (response.isSuccessful && response.body() != null) {
                val overview = response.body()!!
                _accounts.update { list ->
                    list.map { acc ->
                        when (acc.provider) {
                            AccountProvider.GOOGLE_GMAIL -> acc.copy(
                                isConnected = overview.google.isConnected,
                                accountEmailOrHandle = overview.google.accountEmail,
                                syncStatus = if (overview.google.isConnected) "Active" else "Not Connected"
                            )
                            AccountProvider.GITHUB -> acc.copy(
                                isConnected = overview.github.isConnected,
                                accountEmailOrHandle = overview.github.accountName ?: overview.github.accountEmail,
                                syncStatus = if (overview.github.isConnected) "Synced" else "Not Connected"
                            )
                            AccountProvider.LINKEDIN -> acc.copy(
                                isConnected = overview.linkedin.isConnected,
                                accountEmailOrHandle = overview.linkedin.accountName ?: overview.linkedin.accountEmail,
                                syncStatus = if (overview.linkedin.isConnected) "Connected" else "Not Connected"
                            )
                            else -> acc
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Backend unreachable for integrations overview: ${e.message}")
        }
    }

    override suspend fun getConnectUrl(provider: AccountProvider): String? {
        return try {
            val response = when (provider) {
                AccountProvider.GOOGLE_GMAIL -> apiService.getGoogleConnectUrl()
                AccountProvider.GITHUB -> apiService.getGitHubConnectUrl()
                AccountProvider.LINKEDIN -> apiService.getLinkedInConnectUrl()
                AccountProvider.PORTFOLIO -> null
            }
            if (response != null && response.isSuccessful && response.body() != null) {
                response.body()!!["authorization_url"]
            } else null
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get connect URL for $provider: ${e.message}")
            null
        }
    }

    override suspend fun connectGoogleGmail(): Result<ConnectedAccount> {
        return try {
            val response = apiService.syncGoogle()
            if (response.isSuccessful) {
                refreshAccounts()
                val updated = _accounts.value.find { it.provider == AccountProvider.GOOGLE_GMAIL }
                    ?: ConnectedAccount(provider = AccountProvider.GOOGLE_GMAIL, isConnected = true)
                Result.success(updated)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Google sync failed"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.w(TAG, "connectGoogleGmail failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun disconnectAccount(provider: AccountProvider) {
        _accounts.update { list ->
            list.map {
                if (it.provider == provider) {
                    ConnectedAccount(provider = provider, isConnected = false, accountEmailOrHandle = null, syncStatus = "Not Connected")
                } else it
            }
        }
        try {
            apiService.disconnectIntegration(provider.name.lowercase())
        } catch (e: Exception) {
            Log.w(TAG, "Failed to disconnect integration on backend: ${e.message}")
        }
    }

    override suspend fun connectGitHub(handle: String): Result<ConnectedAccount> {
        refreshAccounts()
        val acc = _accounts.value.find { it.provider == AccountProvider.GITHUB }
            ?: ConnectedAccount(provider = AccountProvider.GITHUB, isConnected = false)
        return Result.success(acc)
    }

    override suspend fun connectLinkedIn(profileUrl: String): Result<ConnectedAccount> {
        refreshAccounts()
        val acc = _accounts.value.find { it.provider == AccountProvider.LINKEDIN }
            ?: ConnectedAccount(provider = AccountProvider.LINKEDIN, isConnected = false)
        return Result.success(acc)
    }
}

/**
 * NetworkAssistantRepository connects the Android chat UI with the FastAPI AI Assistant.
 */
class NetworkAssistantRepository(
    private val apiService: JobPilotApiService
) : AssistantRepository {

    private val welcomeMessage = ChatMessage(
        id = "msg-welcome",
        sender = MessageSender.ASSISTANT,
        content = "Hello! I am your JobPilot AI Career Coach. I can analyze your resume, recommend jobs, suggest skills to learn, or help you prepare for upcoming interviews. What would you like to explore today?",
        timestamp = "Just now",
        suggestedActions = listOf(
            "How can I improve my resume match score?",
            "What skills should I learn for Backend roles?",
            "Help me prepare for an upcoming interview"
        )
    )

    private val _messages = MutableStateFlow(listOf(welcomeMessage))
    override val messagesStream: Flow<List<ChatMessage>> = _messages.asStateFlow()

    private var activeConversationId: String? = null

    override suspend fun sendMessage(message: String, jobId: String?): Result<ChatMessage> {
        val userMsg = ChatMessage(
            id = "msg-user-${System.currentTimeMillis()}",
            sender = MessageSender.USER,
            content = message,
            timestamp = "Just now"
        )
        _messages.update { it + userMsg }

        return try {
            val response = apiService.chat(
                ChatRequestDto(
                    message = message,
                    conversationId = activeConversationId,
                    jobId = jobId,
                    contextType = if (jobId != null) "JOB_MATCH" else "GENERAL"
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                activeConversationId = dto.conversationId
                val assistantMsg = ChatMessage(
                    id = dto.messageId,
                    sender = MessageSender.ASSISTANT,
                    content = dto.reply,
                    timestamp = "Just now",
                    isFallback = dto.isFallback,
                    suggestedActions = if (dto.isFallback) {
                        listOf("Review Profile Skills", "Check Job Matches")
                    } else emptyList()
                )
                _messages.update { it + assistantMsg }
                Result.success(assistantMsg)
            } else {
                val fallbackReply = generateLocalCareerAdvice(message)
                val assistantMsg = ChatMessage(
                    id = "msg-fb-${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    content = fallbackReply,
                    timestamp = "Just now",
                    isFallback = true,
                    suggestedActions = listOf("Improve My Profile", "View Job Matches")
                )
                _messages.update { it + assistantMsg }
                Result.success(assistantMsg)
            }
        } catch (e: Exception) {
            Log.w(TAG, "AI Assistant call failed, using grounded fallback: ${e.message}")
            val fallbackReply = generateLocalCareerAdvice(message)
            val assistantMsg = ChatMessage(
                id = "msg-fb-${System.currentTimeMillis()}",
                sender = MessageSender.ASSISTANT,
                content = fallbackReply,
                timestamp = "Just now",
                isFallback = true,
                suggestedActions = listOf("Improve My Profile", "View Job Matches")
            )
            _messages.update { it + assistantMsg }
            Result.success(assistantMsg)
        }
    }

    override suspend fun clearHistory() {
        activeConversationId = null
        _messages.value = listOf(welcomeMessage)
    }

    private fun generateLocalCareerAdvice(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("resume") || lower.contains("match score") ->
                "To increase your match score, ensure your verified skills directly mirror the target job requirements. Projects highlighting FastAPI, PostgreSQL, and modern design patterns provide the fastest score boost."
            lower.contains("skill") || lower.contains("learn") ->
                "Based on current industry demand, strengthening Docker, Kubernetes, and cloud deployment pipelines will bridge the most frequent gaps for Backend Software Engineer positions."
            lower.contains("interview") ->
                "For technical interviews: 1) Be prepared to walk through your system architecture decisions. 2) Practice explaining SQL indexing and database optimization. 3) Frame answers using the STAR method (Situation, Task, Action, Result)."
            else ->
                "I'm here to support your career journey. I can analyze your resume against job openings, identify skill gaps, or draft tailored applications grounded strictly in your verified background."
        }
    }
}

