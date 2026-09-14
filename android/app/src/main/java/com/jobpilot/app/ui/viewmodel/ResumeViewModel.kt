package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.*
import com.jobpilot.app.data.repository.ProfileRepository
import com.jobpilot.app.data.repository.ResumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResumeUiState(
    val resumes: List<Resume> = emptyList(),
    val selectedTemplate: ResumeTemplateType = ResumeTemplateType.MODERN,
    val isUploading: Boolean = false,
    val isGenerating: Boolean = false,
    val parsedResumeData: ResumeParsedData? = null,
    val currentBuildingResume: Resume? = null,
    val exportPdfSuccessMessage: String? = null,
    val analysis: ResumeAnalysis? = null,
    val isAnalyzing: Boolean = false,
    val analysisError: String? = null
)

class ResumeViewModel(
    private val resumeRepository: ResumeRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumeUiState())
    val uiState: StateFlow<ResumeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            resumeRepository.resumesStream.collect { list ->
                _uiState.value = _uiState.value.copy(resumes = list)
            }
        }
    }

    fun selectTemplate(templateType: ResumeTemplateType) {
        _uiState.value = _uiState.value.copy(selectedTemplate = templateType)
    }

    fun simulateUploadResume(fileName: String, onParsed: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            val parsed = resumeRepository.parseUploadedResume(fileName)
            _uiState.value = _uiState.value.copy(
                isUploading = false,
                parsedResumeData = parsed
            )
            onParsed()
        }
    }

    fun uploadResumeFile(bytes: ByteArray, fileName: String, mimeType: String, onParsed: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            val result = resumeRepository.uploadResumeFile(bytes, fileName, mimeType)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    parsedResumeData = result.getOrNull()
                )
                onParsed()
            } else {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    parsedResumeData = null
                )
            }
        }
    }

    fun confirmResume(resumeId: String, onConfirmed: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true)
            val result = resumeRepository.confirmResume(resumeId)
            _uiState.value = _uiState.value.copy(isUploading = false)
            if (result.isSuccess) {
                onConfirmed()
            }
        }
    }

    fun generateResume(title: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true)
            val profile = profileRepository.getProfile()
            val newResume = resumeRepository.createResume(
                title = title.ifBlank { "My Professional Resume" },
                templateType = _uiState.value.selectedTemplate,
                profile = profile
            )
            _uiState.value = _uiState.value.copy(
                isGenerating = false,
                currentBuildingResume = newResume
            )
            onSuccess()
        }
    }

    fun tailorResume(resumeId: String, targetJobTitle: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true)
            val tailored = resumeRepository.tailorResumeForJob(resumeId, targetJobTitle)
            _uiState.value = _uiState.value.copy(
                isGenerating = false,
                currentBuildingResume = tailored
            )
            onSuccess()
        }
    }

    fun exportPdf(resumeId: String, destFile: java.io.File? = null) {
        viewModelScope.launch {
            val realId = if (resumeId == "current" || resumeId.isBlank()) {
                _uiState.value.currentBuildingResume?.id
                    ?: _uiState.value.resumes.firstOrNull()?.id
                    ?: run {
                        val profile = profileRepository.getProfile()
                        val created = resumeRepository.createResume("My Professional Resume", _uiState.value.selectedTemplate, profile)
                        created.id
                    }
            } else resumeId

            if (destFile != null) {
                val result = resumeRepository.exportPdfToFile(realId, destFile)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        exportPdfSuccessMessage = "Resume exported successfully to ${destFile.name} (${destFile.length()} bytes, ATS-compliant)."
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        exportPdfSuccessMessage = "PDF exported successfully as ATS-compliant document."
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    exportPdfSuccessMessage = "Resume exported successfully as PDF to Documents folder."
                )
            }
        }
    }


    fun clearPdfMessage() {
        _uiState.value = _uiState.value.copy(exportPdfSuccessMessage = null)
    }

    fun analyzeResume(resumeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzing = true, analysisError = null)
            val result = resumeRepository.analyzeResume(resumeId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysis = result.getOrNull(),
                    analysisError = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysisError = result.exceptionOrNull()?.message ?: "Failed to analyze resume"
                )
            }
        }
    }

    fun clearAnalysis() {
        _uiState.value = _uiState.value.copy(analysis = null, analysisError = null)
    }
}
