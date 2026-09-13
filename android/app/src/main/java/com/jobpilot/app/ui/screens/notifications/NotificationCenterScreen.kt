package com.jobpilot.app.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobpilot.app.data.model.NotificationItem
import com.jobpilot.app.data.model.NotificationType
import com.jobpilot.app.ui.components.EmptyState
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterScreen(
    viewModel: NotificationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToApplication: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Center", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.unreadCount > 0) {
                        IconButton(onClick = { viewModel.markAllAsRead() }) {
                            Icon(Icons.Default.DoneAll, contentDescription = "Mark all read", tint = Orange500)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        if (uiState.notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                EmptyState(
                    title = "All Caught Up",
                    message = "You have no new notifications. Application milestones and interview invites will appear here."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(uiState.notifications) { item ->
                    NotificationCardItem(
                        item = item,
                        onClick = {
                            viewModel.markAsRead(item.id)
                            if (item.relatedId != null) {
                                onNavigateToApplication(item.relatedId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCardItem(
    item: NotificationItem,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (item.isRead) BgWhite else Orange50.copy(alpha = 0.5f),
        borderColor = if (item.isRead) BorderGlass else Orange400.copy(alpha = 0.4f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (item.isRead) Slate200 else Orange500)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Slate900,
                        fontWeight = if (item.isRead) FontWeight.SemiBold else FontWeight.Bold
                    )
                    Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate600
                )
            }
        }
    }
}
