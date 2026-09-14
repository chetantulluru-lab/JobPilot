package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AuthRepository {
    val currentUserStream: Flow<User?>
    fun getCurrentUser(): User?
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(fullName: String, email: String, password: String): Result<User>
    suspend fun startRegistration(fullName: String, email: String, password: String): Result<String>
    suspend fun verifyRegistrationOtp(email: String, otp: String): Result<User>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun startForgotPassword(email: String): Result<String>
    suspend fun verifyForgotPassword(email: String, otp: String, newPassword: String): Result<Unit>
    suspend fun logout()
    fun hasActiveSession(): Boolean
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUserStream: Flow<User?> = _currentUser.asStateFlow()
    private var _onboardingDone = false

    override fun getCurrentUser(): User? = _currentUser.value

    override fun hasActiveSession(): Boolean = _currentUser.value != null

    override fun isOnboardingCompleted(): Boolean = _onboardingDone

    override fun setOnboardingCompleted(completed: Boolean) {
        _onboardingDone = completed
    }

    override suspend fun login(email: String, password: String): Result<User> {
        delay(600)
        return if (email.isNotBlank() && password.length >= 6) {
            val user = User(
                id = "mock-user-1",
                fullName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                profileStrength = 0
            )
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Please enter a valid email and password (min 6 characters)."))
        }
    }

    override suspend fun register(fullName: String, email: String, password: String): Result<User> {
        delay(600)
        return if (fullName.isNotBlank() && email.isNotBlank() && password.length >= 6) {
            val user = User(
                id = "user-${System.currentTimeMillis()}",
                fullName = fullName,
                email = email,
                profileStrength = 0
            )
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Please fill in all fields with valid information."))
        }
    }

    override suspend fun startRegistration(fullName: String, email: String, password: String): Result<String> {
        delay(400)
        return Result.success("Verification code sent to $email")
    }

    override suspend fun verifyRegistrationOtp(email: String, otp: String): Result<User> {
        delay(400)
        val user = User(
            id = "user-${System.currentTimeMillis()}",
            fullName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = email,
            profileStrength = 0
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        delay(400)
        return if (email.contains("@")) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Please enter a valid email address."))
        }
    }

    override suspend fun startForgotPassword(email: String): Result<String> {
        delay(400)
        return Result.success("Password reset code sent to $email")
    }

    override suspend fun verifyForgotPassword(email: String, otp: String, newPassword: String): Result<Unit> {
        delay(400)
        return Result.success(Unit)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }
}
