package com.jobpilot.app.data.model

enum class ApplicationStatus(val displayName: String) {
    APPLIED("Applied"),
    APPLICATION_RECEIVED("Application Received"),
    ASSESSMENT("Assessment"),
    INTERVIEW("Interview"),
    SHORTLISTED("Shortlisted"),
    OFFER("Offer"),
    REJECTED("Rejected"),
    OTHER("Other")
}

data class JobApplication(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val location: String,
    val currentStatus: ApplicationStatus,
    val appliedDate: String,
    val lastUpdated: String,
    val notes: String = "",
    val recruiterContact: String? = null,
    val matchScoreAtApplication: Int = 92,
    val timeline: List<ApplicationEvent>
)

data class ApplicationEvent(
    val id: String,
    val status: ApplicationStatus,
    val title: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean = true
)
