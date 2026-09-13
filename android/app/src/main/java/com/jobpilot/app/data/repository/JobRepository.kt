package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface JobRepository {
    val jobsStream: Flow<List<Job>>
    fun getAllJobs(): List<Job>
    fun getJobById(id: String): Job?
    suspend fun filterJobs(query: String, workMode: String? = null): List<Job>
}

class MockJobRepository : JobRepository {
    private val _jobs = MutableStateFlow(MockDataProvider.mockJobs)
    override val jobsStream: Flow<List<Job>> = _jobs.asStateFlow()

    override fun getAllJobs(): List<Job> = _jobs.value

    override fun getJobById(id: String): Job? = _jobs.value.find { it.id == id }

    override suspend fun filterJobs(query: String, workMode: String?): List<Job> {
        return _jobs.value.filter { job ->
            val matchesQuery = query.isBlank() || 
                job.title.contains(query, ignoreCase = true) ||
                job.company.contains(query, ignoreCase = true) ||
                job.requirements.any { it.contains(query, ignoreCase = true) }
            val matchesMode = workMode == null || workMode == "All" || job.workMode.equals(workMode, ignoreCase = true)
            matchesQuery && matchesMode
        }
    }
}
