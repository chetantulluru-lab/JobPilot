package com.jobpilot.app.data.repository

import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.network.*

interface InterviewRepository {
    suspend fun startMockInterview(
        mode: String,
        targetRole: String?,
        experienceLevel: String,
        resumeId: String?
    ): Result<MockInterviewSession>

    suspend fun submitMockInterview(
        sessionId: String,
        answers: List<Pair<Int, String>>,
        facePresenceScore: Float
    ): Result<MockInterviewReport>

    suspend fun getInterviewHistory(): Result<List<MockInterviewSession>>
    suspend fun getInterviewReport(sessionId: String): Result<MockInterviewReport>
}

class NetworkInterviewRepository(
    private val apiService: JobPilotApiService,
    private val fallback: MockInterviewRepository = MockInterviewRepository()
) : InterviewRepository {

    override suspend fun startMockInterview(
        mode: String,
        targetRole: String?,
        experienceLevel: String,
        resumeId: String?
    ): Result<MockInterviewSession> {
        return try {
            val response = apiService.startMockInterview(
                MockInterviewStartRequestDto(
                    mode = mode,
                    targetRole = targetRole,
                    experienceLevel = experienceLevel,
                    resumeId = resumeId
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(
                    MockInterviewSession(
                        id = dto.id,
                        title = dto.title,
                        mode = dto.mode,
                        targetRole = dto.targetRole,
                        experienceLevel = dto.experienceLevel,
                        status = dto.status,
                        questions = dto.questions.map { q ->
                            InterviewQuestion(
                                id = q.id,
                                category = q.category,
                                question = q.question,
                                hints = q.hints,
                                expectedConcepts = q.expectedConcepts
                            )
                        },
                        overallScore = dto.overallScore
                    )
                )
            } else {
                fallback.startMockInterview(mode, targetRole, experienceLevel, resumeId)
            }
        } catch (e: Exception) {
            fallback.startMockInterview(mode, targetRole, experienceLevel, resumeId)
        }
    }

    override suspend fun submitMockInterview(
        sessionId: String,
        answers: List<Pair<Int, String>>,
        facePresenceScore: Float
    ): Result<MockInterviewReport> {
        return try {
            val req = MockInterviewSubmitRequestDto(
                answers = answers.map { InterviewCandidateAnswerDto(it.first, it.second) },
                facePresenceScore = facePresenceScore
            )
            val response = apiService.submitMockInterview(sessionId, req)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Result.success(mapReportDto(dto))
            } else {
                fallback.submitMockInterview(sessionId, answers, facePresenceScore)
            }
        } catch (e: Exception) {
            fallback.submitMockInterview(sessionId, answers, facePresenceScore)
        }
    }

    override suspend fun getInterviewHistory(): Result<List<MockInterviewSession>> {
        return try {
            val response = apiService.getInterviewHistory()
            if (response.isSuccessful && response.body() != null) {
                Result.success(
                    response.body()!!.map { dto ->
                        MockInterviewSession(
                            id = dto.id,
                            title = dto.title,
                            mode = dto.mode,
                            targetRole = dto.targetRole,
                            experienceLevel = dto.experienceLevel,
                            status = dto.status,
                            questions = dto.questions.map { q ->
                                InterviewQuestion(
                                    id = q.id,
                                    category = q.category,
                                    question = q.question,
                                    hints = q.hints,
                                    expectedConcepts = q.expectedConcepts
                                )
                            },
                            overallScore = dto.overallScore
                        )
                    }
                )
            } else {
                fallback.getInterviewHistory()
            }
        } catch (e: Exception) {
            fallback.getInterviewHistory()
        }
    }

    override suspend fun getInterviewReport(sessionId: String): Result<MockInterviewReport> {
        return try {
            val response = apiService.getInterviewReport(sessionId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(mapReportDto(response.body()!!))
            } else {
                fallback.getInterviewReport(sessionId)
            }
        } catch (e: Exception) {
            fallback.getInterviewReport(sessionId)
        }
    }

    private fun mapReportDto(dto: MockInterviewReportDto): MockInterviewReport {
        return MockInterviewReport(
            sessionId = dto.sessionId,
            title = dto.title,
            targetRole = dto.targetRole,
            overallScore = dto.overallScore,
            readinessBadge = dto.readinessBadge,
            technicalScore = dto.technicalScore,
            communicationScore = dto.communicationScore,
            problemSolvingScore = dto.problemSolvingScore,
            presenceScore = dto.presenceScore,
            summary = dto.summary,
            keyStrengths = dto.keyStrengths,
            areasForImprovement = dto.areasForImprovement,
            recommendedRoadmapTopics = dto.recommendedRoadmapTopics,
            questionEvaluations = dto.questionEvaluations.map { qe ->
                QuestionEvaluation(
                    questionId = qe.questionId,
                    category = qe.category,
                    question = qe.question,
                    candidateAnswer = qe.candidateAnswer,
                    score = qe.score,
                    feedback = qe.feedback,
                    strengths = qe.strengths,
                    weaknesses = qe.weaknesses,
                    modelAnswer = qe.modelAnswer
                )
            }
        )
    }
}

class MockInterviewRepository : InterviewRepository {
    private val sessions = mutableMapOf<String, MockInterviewSession>()
    private val reports = mutableMapOf<String, MockInterviewReport>()

    override suspend fun startMockInterview(
        mode: String,
        targetRole: String?,
        experienceLevel: String,
        resumeId: String?
    ): Result<MockInterviewSession> {
        val role = targetRole?.ifBlank { "Android Engineer" } ?: "Android Engineer"
        val questions = listOf(
            InterviewQuestion(
                id = 1,
                category = "Introduction",
                question = "Welcome to this interview for the $role position. To start off, could you please introduce yourself, tell me about your background, and share what drives your passion for engineering?",
                hints = listOf("Mention your education, technical stack, and passion for software."),
                expectedConcepts = listOf("Background", "Passion", "Technical focus")
            ),
            InterviewQuestion(
                id = 2,
                category = "Project Deep-Dive",
                question = "Can you walk me through the most technically challenging project you have developed? Explain your architectural decisions, the tech stack you picked, and how you resolved the biggest technical roadblock.",
                hints = listOf("Discuss architecture, trade-offs, and how you overcame a major bug or performance bottleneck."),
                expectedConcepts = listOf("Architecture", "Problem Solving", "Modularity")
            ),
            InterviewQuestion(
                id = 3,
                category = "Technical Core",
                question = "In scalable software systems, how do you handle state management, prevent memory leaks, and design clean, maintainable APIs using modern architectural patterns?",
                hints = listOf("Discuss Unidirectional Data Flow, separation of concerns, and immutability."),
                expectedConcepts = listOf("State Management", "Separation of Concerns", "API Design")
            ),
            InterviewQuestion(
                id = 4,
                category = "Problem Solving",
                question = "Imagine a service you shipped experiences a sudden spike in latency and 500 errors in production. Walk me through your step-by-step triage and troubleshooting methodology.",
                hints = listOf("Discuss rollback, metrics/logs inspection, root cause analysis, and post-mortem."),
                expectedConcepts = listOf("Incident Triage", "Metrics", "Root Cause Analysis")
            ),
            InterviewQuestion(
                id = 5,
                category = "Behavioral",
                question = "Tell me about a time you worked on a project with ambiguous requirements or a tight deadline. How did you prioritize tasks, communicate with stakeholders, and ensure quality delivery?",
                hints = listOf("Use STAR format: Situation, Task, Action, Result."),
                expectedConcepts = listOf("STAR Format", "Communication", "Prioritization")
            )
        )

        val id = "mock-sess-${System.currentTimeMillis()}"
        val sess = MockInterviewSession(
            id = id,
            title = "$role Mock Interview ($experienceLevel)",
            mode = mode,
            targetRole = role,
            experienceLevel = experienceLevel,
            status = "IN_PROGRESS",
            questions = questions
        )
        sessions[id] = sess
        return Result.success(sess)
    }

    override suspend fun submitMockInterview(
        sessionId: String,
        answers: List<Pair<Int, String>>,
        facePresenceScore: Float
    ): Result<MockInterviewReport> {
        val sess = sessions[sessionId]
        val role = sess?.targetRole ?: "Software Engineer"

        val evaluations = answers.map { (qid, text) ->
            val words = text.split(" ").filter { it.isNotBlank() }.size
            val score = if (words > 30) 88 else if (words > 15) 72 else 50
            QuestionEvaluation(
                questionId = qid,
                category = if (qid == 1) "Introduction" else if (qid == 2) "Project Deep-Dive" else if (qid == 3) "Technical Core" else if (qid == 4) "Problem Solving" else "Behavioral",
                question = sess?.questions?.find { it.id == qid }?.question ?: "Interview Question $qid",
                candidateAnswer = text.ifBlank { "No answer provided." },
                score = score,
                feedback = if (words > 30) "Articulate response demonstrating solid understanding and structured delivery." else "Good start. Expand more on specific implementation challenges and trade-offs.",
                strengths = listOf("Directly addressed the prompt", "Communicated key ideas clearly"),
                weaknesses = if (words <= 30) listOf("Could include more technical specifics and metrics") else emptyList(),
                modelAnswer = "In professional engineering, focus on structuring your answer with context, concrete technical choices, and quantifiable outcomes."
            )
        }

        val presence = facePresenceScore.toInt().coerceIn(0, 100)
        val overall = (evaluations.map { it.score }.average().toInt() * 0.85 + presence * 0.15).toInt().coerceIn(0, 100)

        val report = MockInterviewReport(
            sessionId = sessionId,
            title = "$role Mock Interview Report",
            targetRole = role,
            overallScore = overall,
            readinessBadge = if (overall >= 80) "Ready for Industry Interviews 🌟" else "Solid Foundation — Practice Recommended 🚀",
            technicalScore = 84,
            communicationScore = 82,
            problemSolvingScore = 80,
            presenceScore = presence,
            summary = "Well-structured session. You articulated your thoughts with clarity and maintained consistent camera presence.",
            keyStrengths = listOf("Clear communication", "Demonstrated domain knowledge", "Maintained $presence% camera engagement"),
            areasForImprovement = listOf("Quantify project impact with metrics", "Structure answers using STAR framework"),
            recommendedRoadmapTopics = listOf("$role System Architecture", "Production Reliability & Testing"),
            questionEvaluations = evaluations
        )
        reports[sessionId] = report
        return Result.success(report)
    }

    override suspend fun getInterviewHistory(): Result<List<MockInterviewSession>> {
        return Result.success(sessions.values.toList())
    }

    override suspend fun getInterviewReport(sessionId: String): Result<MockInterviewReport> {
        val report = reports[sessionId] ?: MockInterviewReport(
            sessionId = sessionId,
            title = "Mock Interview Report",
            targetRole = "Software Engineer",
            overallScore = 82,
            readinessBadge = "Ready for Industry Interviews 🌟",
            technicalScore = 85,
            communicationScore = 80,
            problemSolvingScore = 80,
            presenceScore = 90,
            summary = "Solid interview performance with strong foundational grasp.",
            keyStrengths = listOf("Clear communication", "Good technical concepts"),
            areasForImprovement = listOf("Add metrics to project descriptions"),
            recommendedRoadmapTopics = listOf("Clean Architecture", "System Design"),
            questionEvaluations = emptyList()
        )
        return Result.success(report)
    }
}
