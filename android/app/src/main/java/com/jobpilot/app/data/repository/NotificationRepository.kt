package com.jobpilot.app.data.repository

import com.jobpilot.app.data.mock.MockDataProvider
import com.jobpilot.app.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface NotificationRepository {
    val notificationsStream: Flow<List<NotificationItem>>
    fun getAllNotifications(): List<NotificationItem>
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()
}

class MockNotificationRepository : NotificationRepository {
    private val _notifications = MutableStateFlow(MockDataProvider.mockNotifications)
    override val notificationsStream: Flow<List<NotificationItem>> = _notifications.asStateFlow()

    override fun getAllNotifications(): List<NotificationItem> = _notifications.value

    override suspend fun markAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    override suspend fun markAllAsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }
}
