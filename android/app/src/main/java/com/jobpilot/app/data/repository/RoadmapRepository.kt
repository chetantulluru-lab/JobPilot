package com.jobpilot.app.data.repository

import android.util.Log
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.network.*
import org.json.JSONObject

private const val TAG = "RoadmapRepository"

interface RoadmapRepository {
    suspend fun getCourseCatalog(): Result<List<CourseCatalogItemDto>>
    suspend fun generateRoadmapFromCourses(courseIds: List<String>, duration: String = "6 Months"): Result<RoadmapDetail>
    suspend fun askCurriculumAssistant(topic: String, question: String, dayNumber: Int? = null): Result<String>
    suspend fun getSuggestions(query: String): Result<List<String>>
    suspend fun generateRoadmap(goal: String, duration: String = "6 Months"): Result<RoadmapDetail>
    suspend fun getRoadmaps(): Result<List<RoadmapSummary>>
    suspend fun getRoadmapById(id: String): Result<RoadmapDetail>
    suspend fun completeDay(dayId: String): Result<DayCompleteResult>
    suspend fun getPhaseResources(phaseId: String, language: String? = null): Result<List<RoadmapResource>>
    suspend fun addSkillsToResume(roadmapId: String): Result<AddSkillsResult>
    suspend fun deleteRoadmap(roadmapId: String): Result<Unit>
}

class NetworkRoadmapRepository(
    private val apiService: JobPilotApiService
) : RoadmapRepository {

    override suspend fun getCourseCatalog(): Result<List<CourseCatalogItemDto>> {
        return try {
            val response = apiService.getCourseCatalog()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.courses)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.w(TAG, "getCourseCatalog error: ${e.message}")
            Result.success(emptyList())
        }
    }

    override suspend fun generateRoadmapFromCourses(courseIds: List<String>, duration: String): Result<RoadmapDetail> {
        return try {
            val response = apiService.generateRoadmapFromCourses(
                RoadmapGenerateFromCoursesRequestDto(courseIds = courseIds, duration = duration)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapDetailDtoToModel(response.body()!!))
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Failed to generate roadmap from selected courses."
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "generateRoadmapFromCourses error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun askCurriculumAssistant(topic: String, question: String, dayNumber: Int?): Result<String> {
        return try {
            val response = apiService.askCurriculumAssistant(
                CurriculumAssistantRequestDto(topic = topic, question = question, dayNumber = dayNumber)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.answer)
            } else {
                Result.failure(Exception("Could not get answer from Curriculum Assistant"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "askCurriculumAssistant error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getSuggestions(query: String): Result<List<String>> {
        return try {
            val response = apiService.getRoadmapSuggestions(query)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.suggestions)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch suggestions: ${e.message}")
            Result.success(emptyList())
        }
    }

    override suspend fun generateRoadmap(goal: String, duration: String): Result<RoadmapDetail> {
        return try {
            val response = apiService.generateRoadmap(
                RoadmapGenerateRequestDto(goal = goal, duration = duration)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapDetailDtoToModel(response.body()!!))
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Roadmap couldn't be generated. Please try again."
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "generateRoadmap error: ${e.message}", e)
            Result.failure(Exception("Roadmap couldn't be generated. Please try again."))
        }
    }

    override suspend fun getRoadmaps(): Result<List<RoadmapSummary>> {
        return try {
            val response = apiService.getRoadmaps()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { dto ->
                    RoadmapSummary(
                        id = dto.id,
                        userId = dto.userId,
                        title = dto.title,
                        goal = dto.goal,
                        duration = dto.duration,
                        totalDays = dto.totalDays,
                        completedDays = dto.completedDays,
                        progressPercentage = dto.progressPercentage,
                        isCompleted = dto.isCompleted,
                        currentPhaseTitle = dto.currentPhaseTitle,
                        currentDayTopic = dto.currentDayTopic,
                        createdAt = dto.createdAt
                    )
                }
                Result.success(list)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.w(TAG, "getRoadmaps error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getRoadmapById(id: String): Result<RoadmapDetail> {
        return try {
            val response = apiService.getRoadmapById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapDetailDtoToModel(response.body()!!))
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Roadmap not found"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getRoadmapById error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun completeDay(dayId: String): Result<DayCompleteResult> {
        return try {
            val response = apiService.completeRoadmapDay(dayId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    DayCompleteResult(
                        isCompleted = dto.isCompleted,
                        progressPercentage = dto.progressPercentage,
                        completedDays = dto.completedDays,
                        totalDays = dto.totalDays,
                        phaseUnlocked = dto.phaseUnlocked,
                        roadmapCompleted = dto.roadmapCompleted,
                        currentStreak = dto.currentStreak,
                        skillsLearned = dto.skillsLearned
                    )
                )
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Failed to mark day as complete"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "completeDay error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getPhaseResources(phaseId: String, language: String?): Result<List<RoadmapResource>> {
        return try {
            val response = apiService.getPhaseResources(phaseId, language)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { mapResourceDtoToModel(it) }
                Result.success(list)
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.w(TAG, "getPhaseResources error: ${e.message}")
            Result.success(emptyList())
        }
    }

    override suspend fun addSkillsToResume(roadmapId: String): Result<AddSkillsResult> {
        return try {
            val response = apiService.addSkillsToResume(roadmapId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    AddSkillsResult(
                        status = dto.status,
                        message = dto.message,
                        addedSkills = dto.addedSkills
                    )
                )
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Failed to add skills to profile"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "addSkillsToResume error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteRoadmap(roadmapId: String): Result<Unit> {
        return try {
            val response = apiService.deleteRoadmap(roadmapId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorJson = response.errorBody()?.string()
                val message = parseErrorMessage(errorJson) ?: "Failed to delete roadmap"
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteRoadmap error: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun mapDetailDtoToModel(dto: RoadmapDetailResponseDto): RoadmapDetail {
        return RoadmapDetail(
            id = dto.id,
            userId = dto.userId,
            title = dto.title,
            goal = dto.goal,
            duration = dto.duration,
            totalDays = dto.totalDays,
            completedDays = dto.completedDays,
            progressPercentage = dto.progressPercentage,
            isCompleted = dto.isCompleted,
            skillsLearned = dto.skillsLearned,
            phases = dto.phases.map { phaseDto ->
                RoadmapPhase(
                    id = phaseDto.id,
                    roadmapId = phaseDto.roadmapId,
                    phaseNumber = phaseDto.phaseNumber,
                    title = phaseDto.title,
                    description = phaseDto.description,
                    isUnlocked = phaseDto.isUnlocked,
                    isCompleted = phaseDto.isCompleted,
                    projectTitle = phaseDto.projectTitle,
                    projectDescription = phaseDto.projectDescription,
                    days = phaseDto.days.map { dayDto ->
                        RoadmapDay(
                            id = dayDto.id,
                            roadmapId = dayDto.roadmapId,
                            phaseId = dayDto.phaseId,
                            dayNumber = dayDto.dayNumber,
                            topic = dayDto.topic,
                            learningObjective = dayDto.learningObjective,
                            subtopics = dayDto.subtopics,
                            practiceTasks = dayDto.practiceTasks.map { taskDto ->
                                PracticeTask(
                                    title = taskDto.title,
                                    description = taskDto.description,
                                    expectedOutput = taskDto.expectedOutput
                                )
                            },
                            isCompleted = dayDto.isCompleted,
                            completedAt = dayDto.completedAt
                        )
                    },
                    resources = phaseDto.resources.map { mapResourceDtoToModel(it) }
                )
            },
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }

    private fun mapResourceDtoToModel(dto: RoadmapResourceResponseDto): RoadmapResource {
        return RoadmapResource(
            id = dto.id,
            dayId = dto.dayId,
            phaseId = dto.phaseId,
            title = dto.title,
            url = dto.url,
            language = dto.language,
            resourceType = dto.resourceType,
            source = dto.source
        )
    }

    private fun parseErrorMessage(json: String?): String? {
        if (json.isNullOrBlank()) return null
        return try {
            val obj = JSONObject(json)
            if (obj.has("detail")) obj.getString("detail") else null
        } catch (_: Exception) {
            null
        }
    }
}

class MockRoadmapRepository : RoadmapRepository {
    override suspend fun getCourseCatalog(): Result<List<CourseCatalogItemDto>> = Result.success(emptyList())
    override suspend fun generateRoadmapFromCourses(courseIds: List<String>, duration: String): Result<RoadmapDetail> =
        Result.failure(Exception("Mock unavailable"))
    override suspend fun askCurriculumAssistant(topic: String, question: String, dayNumber: Int?): Result<String> =
        Result.success("Curriculum Assistant answer for $topic")
    override suspend fun getSuggestions(query: String): Result<List<String>> = Result.success(emptyList())
    override suspend fun generateRoadmap(goal: String, duration: String): Result<RoadmapDetail> =
        Result.failure(Exception("Roadmap couldn't be generated. Please try again."))
    override suspend fun getRoadmaps(): Result<List<RoadmapSummary>> = Result.success(emptyList())
    override suspend fun getRoadmapById(id: String): Result<RoadmapDetail> =
        Result.failure(Exception("Roadmap not found"))
    override suspend fun completeDay(dayId: String): Result<DayCompleteResult> =
        Result.failure(Exception("Backend unavailable"))
    override suspend fun getPhaseResources(phaseId: String, language: String?): Result<List<RoadmapResource>> =
        Result.success(emptyList())
    override suspend fun addSkillsToResume(roadmapId: String): Result<AddSkillsResult> =
        Result.failure(Exception("Backend unavailable"))
    override suspend fun deleteRoadmap(roadmapId: String): Result<Unit> = Result.success(Unit)
}

