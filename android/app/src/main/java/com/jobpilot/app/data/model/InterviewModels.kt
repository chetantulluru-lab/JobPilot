package com.jobpilot.app.data.model

data class InterviewQuestion(
    val id: Int,
    val category: String,
    val question: String,
    val hints: List<String> = emptyList(),
    val expectedConcepts: List<String> = emptyList()
)

data class MockInterviewSession(
    val id: String,
    val title: String,
    val mode: String,
    val targetRole: String,
    val experienceLevel: String,
    val status: String,
    val questions: List<InterviewQuestion> = emptyList(),
    val overallScore: Int? = null
)

data class QuestionEvaluation(
    val questionId: Int,
    val category: String,
    val question: String,
    val candidateAnswer: String,
    val score: Int,
    val feedback: String,
    val strengths: List<String> = emptyList(),
    val weaknesses: List<String> = emptyList(),
    val modelAnswer: String
)

data class MockInterviewReport(
    val sessionId: String,
    val title: String,
    val targetRole: String,
    val overallScore: Int,
    val readinessBadge: String,
    val technicalScore: Int,
    val communicationScore: Int,
    val problemSolvingScore: Int,
    val presenceScore: Int,
    val summary: String,
    val keyStrengths: List<String> = emptyList(),
    val areasForImprovement: List<String> = emptyList(),
    val recommendedRoadmapTopics: List<String> = emptyList(),
    val questionEvaluations: List<QuestionEvaluation> = emptyList()
)

data class OutreachContent(
    val jobTitle: String,
    val company: String,
    val linkedinNote: String,
    val coldEmailSubject: String,
    val coldEmailBody: String,
    val coverLetter: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val explanation: String,
    val correctOptionIndex: Int? = null
)

data class DailyQuiz(
    val dayId: String,
    val dayTitle: String,
    val questions: List<QuizQuestion>
)

data class QuizQuestionResult(
    val questionId: Int,
    val isCorrect: Boolean,
    val correctOptionIndex: Int,
    val selectedOptionIndex: Int,
    val explanation: String
)

data class QuizResult(
    val dayId: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scorePercentage: Int,
    val passed: Boolean = false,
    val dayCompleted: Boolean,
    val currentStreak: Int,
    val feedback: String,
    val questionResults: List<QuizQuestionResult> = emptyList()
)

data class RoadmapNote(
    val id: String,
    val roadmapId: String,
    val dayId: String,
    val noteText: String,
    val isBookmarked: Boolean
)

data class BookmarkedDay(
    val dayId: String,
    val roadmapId: String,
    val roadmapTitle: String,
    val dayNumber: Int,
    val dayTitle: String,
    val noteText: String,
    val isBookmarked: Boolean
)

data class TailoredResumeResult(
    val originalResumeId: String,
    val tailoredResumeId: String,
    val targetJobTitle: String,
    val targetCompany: String,
    val matchScoreBefore: Int,
    val matchScoreAfter: Int,
    val matchedSkills: List<String> = emptyList(),
    val missingSkills: List<String> = emptyList(),
    val tailoredSummary: String,
    val pdfDownloadUrl: String? = null
)
