package com.jobpilot.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.AuthViewModel

data class SettingsRowItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    onNavigateToConnectedAccounts: () -> Unit,
    onLogout: () -> Unit
) {
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val settingsItems = listOf(
        SettingsRowItem(
            title = "Connected Accounts",
            subtitle = "Manage Google / Gmail OAuth, GitHub & LinkedIn",
            icon = Icons.Default.Link,
            onClick = onNavigateToConnectedAccounts
        ),
        SettingsRowItem(
            title = "Notification Preferences",
            subtitle = "Configure interview alerts, matches, and reminders",
            icon = Icons.Default.Notifications,
            onClick = {}
        ),
        SettingsRowItem(
            title = "Privacy & Data Policy",
            subtitle = "Your career information is private and encrypted",
            icon = Icons.Default.Shield,
            onClick = { showPrivacyDialog = true }
        ),
        SettingsRowItem(
            title = "Account Security",
            subtitle = "Biometric access, passwords, and sessions",
            icon = Icons.Default.Lock,
            onClick = {}
        ),
        SettingsRowItem(
            title = "Appearance",
            subtitle = "Light Mode, Glassmorphism and AI Glow settings",
            icon = Icons.Default.Palette,
            onClick = {}
        ),
        SettingsRowItem(
            title = "About JobPilot",
            subtitle = "Version 1.0.0 (Android 11+ / API 30+)",
            icon = Icons.Default.Info,
            onClick = { showAboutDialog = true }
        ),
        SettingsRowItem(
            title = "Help & Support",
            subtitle = "Get assistance, report issues, and send feedback",
            icon = Icons.Default.HelpOutline,
            onClick = {}
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(settingsItems.size) { index ->
                val item = settingsItems[index]
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite,
                    onClick = item.onClick
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = JobPilotShapes.medium,
                            color = Orange50,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(item.icon, contentDescription = null, tint = Orange500, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, style = MaterialTheme.typography.titleMedium, color = Slate900)
                            Text(text = item.subtitle, style = MaterialTheme.typography.bodySmall, color = Slate500)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate400)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = GapOrangeBg,
                    borderColor = GapOrange.copy(alpha = 0.2f),
                    onClick = {
                        authViewModel.logout(onLogout)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = GapOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log Out from JobPilot",
                            color = GapOrange,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About JobPilot") },
            text = {
                Text(
                    text = "JobPilot Android Edition\nVersion 1.0.0\n\"Your career. Piloted by AI.\"\n\nEngineered with Jetpack Compose, Material 3, and Kotlin Coroutines. Prepared for FastAPI and NLP backend integration.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("OK", color = Orange500) }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Principles") },
            text = {
                Text(
                    text = "JobPilot respects your privacy. We never store third-party passwords (such as Gmail, GitHub, or LinkedIn). All account integrations use standard OAuth 2.0. Your career data is used exclusively to power job matching and profile building.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Got it", color = Orange500) }
            }
        )
    }
}
