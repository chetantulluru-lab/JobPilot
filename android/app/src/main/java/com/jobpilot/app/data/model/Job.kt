package com.jobpilot.app.data.model

enum class MatchTier {
    EXCEPTIONAL, // 90%+
    STRONG,      // 75%-89%
    MODERATE,    // 60%-74%
    DEVELOPING   // <60%
}

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val companyLogoUrl: String? = null,
    val location: String,
    val workMode: String, // Remote, Hybrid, On-site
    val employmentType: String, // Internship, Full-time
    val stipendOrSalary: String,
    val description: String,
    val postedDaysAgo: Int,
    val requirements: List<String>,
    val matchDetails: JobMatch,
    val source: String? = "Direct",
    val applicationUrl: String? = null
)

data class JobMatch(
    val matchScore: Int? = null,
    val matchTier: MatchTier? = null,
    val strongMatches: List<String> = emptyList(),
    val missingSkills: List<String> = emptyList(),
    val partialMatches: List<String> = emptyList(),
    val whyItMatchesExplanation: String = "",
    val improvementPlan: List<ImprovementStep> = emptyList(),
    val isProfileInsufficient: Boolean = false
)

data class ImprovementStep(
    val stepNumber: Int,
    val title: String,
    val actionDescription: String,
    val estimatedDays: Int = 5,
    val recommendedResource: String = "Interactive Course & Project"
)
