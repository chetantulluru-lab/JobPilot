package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.ChatMessage
import com.jobpilot.app.data.repository.AssistantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AssistantUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val inputText: String = ""
)

class AssistantViewModel(
    private val assistantRepository: AssistantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            assistantRepository.messagesStream.collect { msgList ->
                _uiState.update { it.copy(messages = msgList) }
            }
        }
    }

    fun onInputChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun sendMessage(content: String? = null, jobId: String? = null) {
        val textToSend = content ?: _uiState.value.inputText.trim()
        if (textToSend.isBlank()) return

        _uiState.update { it.copy(isSending = true, inputText = "", errorMessage = null) }

        viewModelScope.launch {
            val result = assistantRepository.sendMessage(textToSend, jobId)
            result.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isSending = false,
                        errorMessage = err.message ?: "Failed to send message"
                    )
                }
            }
            result.onSuccess {
                _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            assistantRepository.clearHistory()
        }
    }
}
