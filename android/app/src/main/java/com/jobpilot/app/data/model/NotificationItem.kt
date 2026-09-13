package com.jobpilot.app.data.model

enum class NotificationType {
    INTERVIEW_UPDATE,
    APPLICATION_UPDATE,
    PROFILE_IMPROVEMENT,
    NEW_JOB_MATCH,
    GMAIL_EVENT
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val relatedId: String? = null // Job ID or Application ID
)
