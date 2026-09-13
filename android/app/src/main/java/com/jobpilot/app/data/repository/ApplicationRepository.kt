package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.ApplicationEvent
import com.jobpilot.app.data.model.ApplicationStatus
import com.jobpilot.app.data.model.JobApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ApplicationRepository {
    val applicationsStream: Flow<List<JobApplication>>
    fun getAllApplications(): List<JobApplication>
    fun getApplicationById(id: String): JobApplication?
    suspend fun addApplication(application: JobApplication)
    suspend fun updateStatus(id: String, newStatus: ApplicationStatus, eventDescription: String)
}

class MockApplicationRepository : ApplicationRepository {
    private val _applications = MutableStateFlow(MockDataProvider.mockApplications)
    override val applicationsStream: Flow<List<JobApplication>> = _applications.asStateFlow()

    override fun getAllApplications(): List<JobApplication> = _applications.value

    override fun getApplicationById(id: String): JobApplication? =
        _applications.value.find { it.id == id }

    override suspend fun addApplication(application: JobApplication) {
        _applications.update { listOf(application) + it }
    }

    override suspend fun updateStatus(id: String, newStatus: ApplicationStatus, eventDescription: String) {
        _applications.update { list ->
            list.map { app ->
                if (app.id == id) {
                    val newEvent = ApplicationEvent(
                        id = "ev-${System.currentTimeMillis()}",
                        status = newStatus,
                        title = newStatus.displayName,
                        description = eventDescription,
                        timestamp = "Just now"
                    )
                    app.copy(
                        currentStatus = newStatus,
                        lastUpdated = "Just now",
                        timeline = app.timeline + newEvent
                    )
                } else app
            }
        }
    }
}
