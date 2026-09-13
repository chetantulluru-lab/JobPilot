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
    val matchDetails: JobMatch
)

data class JobMatch(
    val matchScore: Int,
    val matchTier: MatchTier,
    val strongMatches: List<String>,
    val missingSkills: List<String>,
    val partialMatches: List<String> = emptyList(),
    val whyItMatchesExplanation: String,
    val improvementPlan: List<ImprovementStep> = emptyList()
)

data class ImprovementStep(
    val stepNumber: Int,
    val title: String,
    val actionDescription: String,
    val estimatedDays: Int = 5,
    val recommendedResource: String = "Interactive Course & Project"
)
