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
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun logout()
}

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(MockDataProvider.currentUser)
    override val currentUserStream: Flow<User?> = _currentUser.asStateFlow()

    override fun getCurrentUser(): User? = _currentUser.value

    override suspend fun login(email: String, password: String): Result<User> {
        delay(600)
        return if (email.isNotBlank() && password.length >= 6) {
            val user = MockDataProvider.currentUser.copy(email = email)
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
                profileStrength = 50
            )
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Please fill in all fields with valid information."))
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        delay(400)
        return if (email.contains("@")) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Please enter a valid email address."))
        }
    }

    override suspend fun logout() {
        _currentUser.value = null
    }
}
