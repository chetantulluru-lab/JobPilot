package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jobpilot.app.data.AppContainer

class JobPilotViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(container.authRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(
                    container.profileRepository,
                    container.jobRepository,
                    container.applicationRepository
                ) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(container.profileRepository) as T
            }
            modelClass.isAssignableFrom(JobViewModel::class.java) -> {
                JobViewModel(container.jobRepository) as T
            }
            modelClass.isAssignableFrom(ApplicationViewModel::class.java) -> {
                ApplicationViewModel(container.applicationRepository) as T
            }
            modelClass.isAssignableFrom(ResumeViewModel::class.java) -> {
                ResumeViewModel(
                    container.resumeRepository,
                    container.profileRepository
                ) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                NotificationViewModel(container.notificationRepository) as T
            }
            modelClass.isAssignableFrom(ConnectedAccountsViewModel::class.java) -> {
                ConnectedAccountsViewModel(container.connectedAccountRepository) as T
            }
            modelClass.isAssignableFrom(AssistantViewModel::class.java) -> {
                AssistantViewModel(container.assistantRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")

        }
    }
}
