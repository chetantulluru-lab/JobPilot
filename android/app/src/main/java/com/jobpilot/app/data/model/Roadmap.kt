package com.jobpilot.app.data.model

data class RoadmapSummary(
    val id: String,
    val userId: String,
    val title: String,
    val goal: String,
    val duration: String,
    val totalDays: Int,
    val completedDays: Int,
    val progressPercentage: Int,
    val isCompleted: Boolean,
    val currentPhaseTitle: String? = null,
    val currentDayTopic: String? = null,
    val createdAt: String
)

data class RoadmapDetail(
    val id: String,
    val userId: String,
    val title: String,
    val goal: String,
    val duration: String,
    val totalDays: Int,
    val completedDays: Int,
    val progressPercentage: Int,
    val isCompleted: Boolean,
    val skillsLearned: List<String> = emptyList(),
    val phases: List<RoadmapPhase> = emptyList(),
    val createdAt: String,
    val updatedAt: String
)

data class RoadmapPhase(
    val id: String,
    val roadmapId: String,
    val phaseNumber: Int,
    val title: String,
    val description: String? = null,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val projectTitle: String? = null,
    val projectDescription: String? = null,
    val days: List<RoadmapDay> = emptyList(),
    val resources: List<RoadmapResource> = emptyList()
)

data class RoadmapDay(
    val id: String,
    val roadmapId: String,
    val phaseId: String,
    val dayNumber: Int,
    val topic: String,
    val learningObjective: String? = null,
    val subtopics: List<String> = emptyList(),
    val practiceTasks: List<PracticeTask> = emptyList(),
    val isCompleted: Boolean = false,
    val completedAt: String? = null
)

data class PracticeTask(
    val title: String,
    val description: String,
    val expectedOutput: String? = null
)

data class RoadmapResource(
    val id: String,
    val dayId: String? = null,
    val phaseId: String,
    val title: String,
    val url: String,
    val language: String, // English, Telugu, Hindi
    val resourceType: String, // video, article, doc
    val source: String // YouTube, Docs, GeeksforGeeks, FreeCodeCamp
)

data class DayCompleteResult(
    val isCompleted: Boolean,
    val progressPercentage: Int,
    val completedDays: Int,
    val totalDays: Int,
    val phaseUnlocked: Boolean,
    val roadmapCompleted: Boolean,
    val currentStreak: Int,
    val skillsLearned: List<String> = emptyList()
)

data class AddSkillsResult(
    val status: String,
    val message: String,
    val addedSkills: List<String>
)
