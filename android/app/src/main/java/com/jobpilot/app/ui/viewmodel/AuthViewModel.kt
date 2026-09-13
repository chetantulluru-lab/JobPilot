package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.User
import com.jobpilot.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(currentUser = authRepository.getCurrentUser()))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    currentUser = result.getOrNull(),
                    isLoading = false,
                    errorMessage = null
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    fun register(fullName: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.register(fullName, email, password)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    currentUser = result.getOrNull(),
                    isLoading = false,
                    errorMessage = null
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.sendPasswordReset(email)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Password reset instructions sent to $email"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error sending reset email"
                )
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState(currentUser = null)
            onSuccess()
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
