package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.AccountProvider
import com.jobpilot.app.data.model.ConnectedAccount
import com.jobpilot.app.data.model.EmailEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ConnectedAccountRepository {
    val accountsStream: Flow<List<ConnectedAccount>>
    val emailEventsStream: Flow<List<EmailEvent>>
    suspend fun connectGoogleGmail(): Result<ConnectedAccount>
    suspend fun disconnectAccount(provider: AccountProvider)
    suspend fun connectGitHub(handle: String): Result<ConnectedAccount>
    suspend fun connectLinkedIn(profileUrl: String): Result<ConnectedAccount>
}

class MockConnectedAccountRepository : ConnectedAccountRepository {
    private val _accounts = MutableStateFlow(MockDataProvider.mockConnectedAccounts)
    private val _emailEvents = MutableStateFlow(MockDataProvider.mockEmailEvents)

    override val accountsStream: Flow<List<ConnectedAccount>> = _accounts.asStateFlow()
    override val emailEventsStream: Flow<List<EmailEvent>> = _emailEvents.asStateFlow()

    override suspend fun connectGoogleGmail(): Result<ConnectedAccount> {
        // Simulates Google OAuth 2.0 flow
        delay(1000)
        val updated = ConnectedAccount(
            provider = AccountProvider.GOOGLE_GMAIL,
            isConnected = true,
            accountEmailOrHandle = "chetan.student@gmail.com",
            connectedAt = "Just now",
            syncStatus = "Active (Synced via Google OAuth 2.0)",
            note = "Google OAuth token active. Auto-detects recruiter replies and interview invitations."
        )
        _accounts.update { list ->
            list.map { if (it.provider == AccountProvider.GOOGLE_GMAIL) updated else it }
        }
        return Result.success(updated)
    }

    override suspend fun disconnectAccount(provider: AccountProvider) {
        _accounts.update { list ->
            list.map {
                if (it.provider == provider) {
                    ConnectedAccount(provider = provider, isConnected = false)
                } else it
            }
        }
    }

    override suspend fun connectGitHub(handle: String): Result<ConnectedAccount> {
        delay(600)
        val updated = ConnectedAccount(
            provider = AccountProvider.GITHUB,
            isConnected = true,
            accountEmailOrHandle = handle,
            connectedAt = "Just now",
            syncStatus = "Synced (Repositories verified)"
        )
        _accounts.update { list ->
            list.map { if (it.provider == AccountProvider.GITHUB) updated else it }
        }
        return Result.success(updated)
    }

    override suspend fun connectLinkedIn(profileUrl: String): Result<ConnectedAccount> {
        delay(600)
        val updated = ConnectedAccount(
            provider = AccountProvider.LINKEDIN,
            isConnected = true,
            accountEmailOrHandle = profileUrl,
            connectedAt = "Just now",
            syncStatus = "Connected"
        )
        _accounts.update { list ->
            list.map { if (it.provider == AccountProvider.LINKEDIN) updated else it }
        }
        return Result.success(updated)
    }
}
