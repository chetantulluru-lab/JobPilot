package com.jobpilot.app.data.model

enum class AccountProvider(val displayName: String) {
    GOOGLE_GMAIL("Google / Gmail"),
    GITHUB("GitHub"),
    LINKEDIN("LinkedIn"),
    PORTFOLIO("Personal Portfolio")
}

data class ConnectedAccount(
    val provider: AccountProvider,
    val isConnected: Boolean,
    val accountEmailOrHandle: String? = null,
    val connectedAt: String? = null,
    val syncStatus: String = "Idle",
    val note: String = ""
)

enum class EmailCategory(val displayName: String) {
    APPLICATION_RECEIVED("Application Received"),
    ASSESSMENT("Online Assessment"),
    INTERVIEW("Interview Invitation"),
    SHORTLISTED("Shortlisted"),
    REJECTION("Status Update / Rejection"),
    OFFER("Job Offer"),
    OTHER("Other")
}

data class EmailEvent(
    val id: String,
    val senderName: String,
    val senderEmail: String,
    val subject: String,
    val snippet: String,
    val category: EmailCategory,
    val detectedJobTitle: String?,
    val detectedCompany: String?,
    val receivedDate: String,
    val confidenceScore: Float = 0.94f
)
