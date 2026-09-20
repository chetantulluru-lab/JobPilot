package com.jobpilot.app.data

import android.content.Context
import com.jobpilot.app.data.network.ApiClient
import com.jobpilot.app.data.network.JobPilotApiService
import com.jobpilot.app.data.network.TokenManager
import com.jobpilot.app.data.repository.*

/**
 * Dependency container providing repositories across the app.
 * Structured to be easily swapped with Hilt / Dagger in production.
 */
interface AppContainer {
    val authRepository: AuthRepository
    val profileRepository: ProfileRepository
    val jobRepository: JobRepository
    val applicationRepository: ApplicationRepository
    val resumeRepository: ResumeRepository
    val notificationRepository: NotificationRepository
    val connectedAccountRepository: ConnectedAccountRepository
    val assistantRepository: AssistantRepository
    val roadmapRepository: RoadmapRepository
    val interviewRepository: InterviewRepository
    val tokenManager: TokenManager?
    val apiService: JobPilotApiService?
}

class DefaultAppContainer(private val context: Context? = null) : AppContainer {
    override val tokenManager: TokenManager? by lazy {
        context?.let { TokenManager(it.applicationContext) }
    }

    override val apiService: JobPilotApiService? by lazy {
        tokenManager?.let { ApiClient.getService(it) }
    }

    override val authRepository: AuthRepository by lazy {
        val service = apiService
        val tm = tokenManager
        if (service != null && tm != null) {
            NetworkAuthRepository(service, tm)
        } else {
            MockAuthRepository()
        }
    }

    override val profileRepository: ProfileRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkProfileRepository(service)
        } else {
            MockProfileRepository()
        }
    }

    override val jobRepository: JobRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkJobRepository(service)
        } else {
            MockJobRepository()
        }
    }

    override val applicationRepository: ApplicationRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkApplicationRepository(service)
        } else {
            MockApplicationRepository()
        }
    }

    override val resumeRepository: ResumeRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkResumeRepository(service)
        } else {
            MockResumeRepository()
        }
    }

    override val notificationRepository: NotificationRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkNotificationRepository(service)
        } else {
            MockNotificationRepository()
        }
    }

    override val connectedAccountRepository: ConnectedAccountRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkConnectedAccountRepository(service)
        } else {
            MockConnectedAccountRepository()
        }
    }

    override val assistantRepository: AssistantRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkAssistantRepository(service)
        } else {
            MockAssistantRepository()
        }
    }

    override val roadmapRepository: RoadmapRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkRoadmapRepository(service)
        } else {
            MockRoadmapRepository()
        }
    }

    override val interviewRepository: InterviewRepository by lazy {
        val service = apiService
        if (service != null) {
            NetworkInterviewRepository(service)
        } else {
            MockInterviewRepository()
        }
    }
}

