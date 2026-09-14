package com.jobpilot.app.data.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val profileImageUrl: String? = null,
    val isEmailVerified: Boolean = true,
    val createdAt: String = "2026-09-01",
    val profileStrength: Int = 0
)
