package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ResumeRepository {
    val resumesStream: Flow<List<Resume>>
    fun getAllResumes(): List<Resume>
    suspend fun parseUploadedResume(fileName: String): ResumeParsedData
    suspend fun uploadResumeFile(bytes: ByteArray, fileName: String, mimeType: String): Result<ResumeParsedData>
    suspend fun confirmResume(resumeId: String): Result<Unit>
    suspend fun createResume(title: String, templateType: ResumeTemplateType, profile: CareerProfile): Resume
    suspend fun tailorResumeForJob(resumeId: String, jobTitle: String): Resume
    suspend fun exportPdfToFile(resumeId: String, destFile: java.io.File): Result<java.io.File>
    suspend fun analyzeResume(resumeId: String): Result<ResumeAnalysis>
    suspend fun getResumeAnalysis(resumeId: String): Result<ResumeAnalysis>
}

class MockResumeRepository : ResumeRepository {
    private val _resumes = MutableStateFlow(
        listOf(
            Resume(
                id = "res-1",
                title = "Software Engineer (Primary)",
                templateType = ResumeTemplateType.MODERN,
                lastModified = "Sep 10, 2026",
                profileSnapshot = MockDataProvider.currentProfile,
                isDefault = true
            ),
            Resume(
                id = "res-2",
                title = "Backend Intern Minimal",
                templateType = ResumeTemplateType.MINIMAL,
                lastModified = "Sep 02, 2026",
                profileSnapshot = MockDataProvider.currentProfile,
                isDefault = false
            )
        )
    )
    override val resumesStream: Flow<List<Resume>> = _resumes.asStateFlow()

    override fun getAllResumes(): List<Resume> = _resumes.value

    override suspend fun parseUploadedResume(fileName: String): ResumeParsedData {
        // Simulates NLP processing delay with mock structured extraction
        delay(1200)
        return MockDataProvider.mockParsedResume
    }

    override suspend fun uploadResumeFile(bytes: ByteArray, fileName: String, mimeType: String): Result<ResumeParsedData> {
        delay(1200)
        return Result.success(MockDataProvider.mockParsedResume)
    }

    override suspend fun confirmResume(resumeId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun createResume(title: String, templateType: ResumeTemplateType, profile: CareerProfile): Resume {
        val newResume = Resume(
            id = "res-${System.currentTimeMillis()}",
            title = title,
            templateType = templateType,
            lastModified = "Just now",
            profileSnapshot = profile,
            isDefault = false
        )
        _resumes.update { listOf(newResume) + it }
        return newResume
    }

    override suspend fun tailorResumeForJob(resumeId: String, jobTitle: String): Resume {
        delay(800)
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
        return tailored ?: _resumes.value.first()
    }

    override suspend fun exportPdfToFile(resumeId: String, destFile: java.io.File): Result<java.io.File> {
        return try {
            delay(300)
            val r = _resumes.value.find { it.id == resumeId } ?: _resumes.value.firstOrNull()
            destFile.outputStream().use { stream ->
                val p = r?.profileSnapshot
                val atsData = com.jobpilot.app.util.AtsResumeData(
                    fullName = p?.personalInfo?.fullName ?: "Chetan",
                    email = p?.personalInfo?.email ?: "chetan@example.com",
                    phone = p?.personalInfo?.phone ?: "+91 98765 43210",
                    location = p?.personalInfo?.location ?: "Bengaluru, India",
                    summary = p?.personalInfo?.professionalSummary ?: "Software Engineer passionate about high performance systems.",
                    degree = p?.education?.firstOrNull()?.degree ?: "B.Tech Computer Science",
                    college = p?.education?.firstOrNull()?.college ?: "National Institute of Technology",
                    branch = p?.education?.firstOrNull()?.branch ?: "Computer Science & Engineering",
                    gradYear = p?.education?.firstOrNull()?.endDate ?: "2026",
                    cgpa = p?.education?.firstOrNull()?.grade ?: "8.85 CGPA",
                    skills = p?.skills?.map { it.name } ?: listOf("Python", "Kotlin", "FastAPI", "SQL"),
                    projects = p?.projects?.map {
                        com.jobpilot.app.util.AtsResumeProject(
                            name = it.name,
                            tech = it.technologies.joinToString(", "),
                            description = it.description,
                            link = it.githubUrl ?: ""
                        )
                    } ?: emptyList(),
                    experience = p?.experience?.firstOrNull()?.description ?: "",
                    templateType = r?.templateType ?: com.jobpilot.app.data.model.ResumeTemplateType.MODERN
                )
                com.jobpilot.app.util.AtsResumePdfGenerator.generate(atsData, stream)
            }
            Result.success(destFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeResume(resumeId: String): Result<ResumeAnalysis> {
        delay(600)
        return Result.success(
            ResumeAnalysis(
                resumeId = resumeId,
                atsScore = 78,
                label = "AI-Powered ATS-Style Analysis",
                summary = "Strong technical foundation with clear impact metrics.",
                strengths = listOf("Clear project descriptions", "Relevant skills highlighted"),
                weaknesses = listOf("Add quantifiable achievements to experience section"),
                missingSkills = listOf("Docker", "CI/CD"),
                contentImprovements = listOf("Use active verbs for achievements"),
                formattingNotes = listOf("Standard single-column format recommended"),
                disclaimer = "Informational guidance based on industry standards. JobPilot makes no employment or interview guarantees."
            )
        )
    }

    override suspend fun getResumeAnalysis(resumeId: String): Result<ResumeAnalysis> {
        return analyzeResume(resumeId)
    }
}

