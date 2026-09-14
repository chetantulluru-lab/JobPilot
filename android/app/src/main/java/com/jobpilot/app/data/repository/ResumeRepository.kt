package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.CareerProfile
import com.jobpilot.app.data.model.Resume
import com.jobpilot.app.data.model.ResumeParsedData
import com.jobpilot.app.data.model.ResumeTemplateType
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
            delay(500)
            destFile.writeBytes("%PDF-1.4 Mock PDF JobPilot Resume".toByteArray())
            Result.success(destFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

