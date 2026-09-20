package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.MockInterviewReport
import com.jobpilot.app.data.model.MockInterviewSession
import com.jobpilot.app.data.repository.InterviewRepository
import com.jobpilot.app.data.repository.ResumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InterviewUiState(
    val isStarting: Boolean = false,
    val isSubmitting: Boolean = false,
    val isLoadingReport: Boolean = false,
    val errorMessage: String? = null,
    val mode: String = "resume", // "resume" or "role"
    val targetRole: String = "Full Stack Engineer",
    val experienceLevel: String = "Entry-Level", // "Entry-Level", "Mid-Level", "Senior / Lead"
    val selectedResumeId: String? = null,
    val currentSession: MockInterviewSession? = null,
    val currentQuestionIndex: Int = 0,
    val currentAnswerText: String = "",
    val recordedAnswers: Map<Int, String> = emptyMap(),
    val faceDetected: Boolean = false,
    val faceDetectedCount: Int = 0,
    val totalFramesChecked: Int = 0,
    val report: MockInterviewReport? = null,
    val history: List<MockInterviewSession> = emptyList(),
    val isTtsMuted: Boolean = false
) {
    val facePresencePercentage: Int
        get() = if (totalFramesChecked > 0) {
            ((faceDetectedCount.toFloat() / totalFramesChecked) * 100).toInt().coerceIn(0, 100)
        } else {
            100 // default good presence if camera is not active
        }
}

class InterviewViewModel(
    private val interviewRepository: InterviewRepository,
    private val resumeRepository: ResumeRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterviewUiState())
    val uiState: StateFlow<InterviewUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
        // If there is an existing resume, auto-select it as default
        viewModelScope.launch {
            try {
                val resumes = resumeRepository?.getAllResumes() ?: emptyList()
                val defaultResume = resumes.firstOrNull { it.isDefault } ?: resumes.firstOrNull()
                if (defaultResume != null) {
                    _uiState.value = _uiState.value.copy(selectedResumeId = defaultResume.id)
                }
            } catch (_: Exception) {}
        }
    }

    fun setMode(mode: String) {
        _uiState.value = _uiState.value.copy(mode = mode)
    }

    fun setTargetRole(role: String) {
        _uiState.value = _uiState.value.copy(targetRole = role)
    }

    fun setExperienceLevel(level: String) {
        _uiState.value = _uiState.value.copy(experienceLevel = level)
    }

    fun selectResume(resumeId: String?) {
        _uiState.value = _uiState.value.copy(selectedResumeId = resumeId)
    }

    fun toggleTtsMute() {
        _uiState.value = _uiState.value.copy(isTtsMuted = !_uiState.value.isTtsMuted)
    }

    fun recordFaceDetection(detected: Boolean) {
        val current = _uiState.value
        _uiState.value = current.copy(
            faceDetected = detected,
            faceDetectedCount = current.faceDetectedCount + if (detected) 1 else 0,
            totalFramesChecked = current.totalFramesChecked + 1
        )
    }

    fun onAnswerChanged(text: String) {
        val current = _uiState.value
        val updatedAnswers = current.recordedAnswers.toMutableMap()
        val currentQ = current.currentSession?.questions?.getOrNull(current.currentQuestionIndex)
        if (currentQ != null) {
            updatedAnswers[currentQ.id] = text
        }
        _uiState.value = current.copy(
            currentAnswerText = text,
            recordedAnswers = updatedAnswers
        )
    }

    fun nextQuestion() {
        val current = _uiState.value
        val questions = current.currentSession?.questions ?: return
        if (current.currentQuestionIndex < questions.size - 1) {
            val nextIndex = current.currentQuestionIndex + 1
            val nextQ = questions[nextIndex]
            val savedAnswer = current.recordedAnswers[nextQ.id] ?: ""
            _uiState.value = current.copy(
                currentQuestionIndex = nextIndex,
                currentAnswerText = savedAnswer
            )
        }
    }

    fun prevQuestion() {
        val current = _uiState.value
        val questions = current.currentSession?.questions ?: return
        if (current.currentQuestionIndex > 0) {
            val prevIndex = current.currentQuestionIndex - 1
            val prevQ = questions[prevIndex]
            val savedAnswer = current.recordedAnswers[prevQ.id] ?: ""
            _uiState.value = current.copy(
                currentQuestionIndex = prevIndex,
                currentAnswerText = savedAnswer
            )
        }
    }

    fun startInterview(onSuccess: () -> Unit) {
        val state = _uiState.value
        _uiState.value = state.copy(isStarting = true, errorMessage = null)

        viewModelScope.launch {
            val result = interviewRepository.startMockInterview(
                mode = state.mode,
                targetRole = if (state.mode == "role") state.targetRole else null,
                experienceLevel = state.experienceLevel,
                resumeId = if (state.mode == "resume") state.selectedResumeId else null
            )

            if (result.isSuccess) {
                val session = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isStarting = false,
                    currentSession = session,
                    currentQuestionIndex = 0,
                    currentAnswerText = "",
                    recordedAnswers = emptyMap(),
                    faceDetectedCount = 0,
                    totalFramesChecked = 0
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isStarting = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to start interview"
                )
            }
        }
    }

    fun submitInterview(onSuccess: (String) -> Unit) {
        val state = _uiState.value
        val session = state.currentSession ?: return
        _uiState.value = state.copy(isSubmitting = true, errorMessage = null)

        val answersList = session.questions.map { q ->
            val answer = state.recordedAnswers[q.id] ?: ""
            Pair(q.id, answer)
        }

        val presenceScore = state.facePresencePercentage.toFloat()

        viewModelScope.launch {
            val result = interviewRepository.submitMockInterview(
                sessionId = session.id,
                answers = answersList,
                facePresenceScore = presenceScore
            )

            if (result.isSuccess) {
                val report = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    report = report
                )
                loadHistory()
                onSuccess(session.id)
            } else {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to evaluate interview"
                )
            }
        }
    }

    fun loadReport(sessionId: String) {
        _uiState.value = _uiState.value.copy(isLoadingReport = true, errorMessage = null)
        viewModelScope.launch {
            val result = interviewRepository.getInterviewReport(sessionId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoadingReport = false,
                    report = result.getOrNull()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingReport = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to load report"
                )
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val result = interviewRepository.getInterviewHistory()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(history = result.getOrDefault(emptyList()))
            }
        }
    }
}
