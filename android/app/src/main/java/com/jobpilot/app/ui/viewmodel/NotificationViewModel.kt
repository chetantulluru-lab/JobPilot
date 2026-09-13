package com.jobpilot.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobpilot.app.data.model.NotificationItem
import com.jobpilot.app.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val unreadCount: Int = 0
)

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            notificationRepository.notificationsStream.collect { list ->
                _uiState.value = NotificationUiState(
                    notifications = list,
                    unreadCount = list.count { !it.isRead }
                )
            }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead()
        }
    }
}
