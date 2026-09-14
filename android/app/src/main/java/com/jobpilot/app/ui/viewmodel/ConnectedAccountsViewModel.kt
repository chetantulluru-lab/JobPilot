package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.AccountProvider
import com.jobpilot.app.data.model.ConnectedAccount
import com.jobpilot.app.data.model.EmailEvent
import com.jobpilot.app.data.repository.ConnectedAccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConnectedAccountsUiState(
    val accounts: List<ConnectedAccount> = emptyList(),
    val emailEvents: List<EmailEvent> = emptyList(),
    val isConnectingGoogle: Boolean = false,
    val feedbackMessage: String? = null
)

class ConnectedAccountsViewModel(
    private val repository: ConnectedAccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectedAccountsUiState())
    val uiState: StateFlow<ConnectedAccountsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.accountsStream.collect { list ->
                _uiState.value = _uiState.value.copy(accounts = list)
            }
        }
        viewModelScope.launch {
            repository.emailEventsStream.collect { events ->
                _uiState.value = _uiState.value.copy(emailEvents = events)
            }
        }
    }

    fun connectGmail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConnectingGoogle = true)
            val res = repository.connectGoogleGmail()
            _uiState.value = _uiState.value.copy(
                isConnectingGoogle = false,
                feedbackMessage = if (res.isSuccess) "Gmail connected successfully via Google OAuth 2.0!" else "Connection failed"
            )
        }
    }

    fun disconnect(provider: AccountProvider) {
        viewModelScope.launch {
            repository.disconnectAccount(provider)
            _uiState.value = _uiState.value.copy(feedbackMessage = "Disconnected ${provider.displayName}")
        }
    }

    fun connectGitHub(handle: String) {
        viewModelScope.launch {
            val res = repository.connectGitHub(handle)
            if (res.isSuccess) {
                _uiState.value = _uiState.value.copy(feedbackMessage = "GitHub connected for $handle")
            }
        }
    }

    fun connectLinkedIn(url: String) {
        viewModelScope.launch {
            val res = repository.connectLinkedIn(url)
            if (res.isSuccess) {
                _uiState.value = _uiState.value.copy(feedbackMessage = "LinkedIn profile verified")
            }
        }
    }

    fun startOAuthConnect(provider: AccountProvider, onUrl: (String) -> Unit) {
        viewModelScope.launch {
            val url = repository.getConnectUrl(provider)
            if (url != null) {
                onUrl(url)
            } else {
                _uiState.value = _uiState.value.copy(feedbackMessage = "Failed to obtain authorization link for ${provider.displayName}")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refreshAccounts()
        }
    }

    fun clearFeedback() {
        _uiState.value = _uiState.value.copy(feedbackMessage = null)
    }
}
