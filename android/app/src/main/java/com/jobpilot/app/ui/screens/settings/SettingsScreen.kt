package com.jobpilot.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onLogout: () -> Unit
) {
    val currentThemeMode by com.jobpilot.app.ui.theme.ThemeManager.themeMode.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var selectedTheme by remember(currentThemeMode) { mutableStateOf(currentThemeMode.displayName) }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordSuccess by remember { mutableStateOf(false) }

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showLicensesDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    val settingsItems = listOf(
        SettingsRowItem(
            title = "Appearance & Theme",
            subtitle = "Choose Light theme, Dark theme, or System default",
            icon = Icons.Default.Palette,
            onClick = { showThemeDialog = true }
        ),
        SettingsRowItem(
            title = "Change Password",
            subtitle = "Update your login credentials securely",
            icon = Icons.Default.Lock,
            onClick = {
                currentPassword = ""
                newPassword = ""
                confirmPassword = ""
                passwordError = null
                passwordSuccess = false
                showPasswordDialog = true
            }
        ),
        SettingsRowItem(
            title = "Privacy Policy",
            subtitle = "Your career and resume data is private and encrypted",
            icon = Icons.Default.Shield,
            onClick = { showPrivacyDialog = true }
        ),
        SettingsRowItem(
            title = "Terms of Service",
            subtitle = "Usage guidelines for AI roadmaps and resume services",
            icon = Icons.Default.Description,
            onClick = { showTermsDialog = true }
        ),
        SettingsRowItem(
            title = "Contact Support",
            subtitle = "Reach out for help, bugs, or feature feedback",
            icon = Icons.Default.SupportAgent,
            onClick = { showSupportDialog = true }
        ),
        SettingsRowItem(
            title = "Open Source Licenses",
            subtitle = "Software components that power JobPilot",
            icon = Icons.Default.Code,
            onClick = { showLicensesDialog = true }
        ),
        SettingsRowItem(
            title = "About JobPilot",
            subtitle = "Version 1.0.0 (Production Release)",
            icon = Icons.Default.Info,
            onClick = { showAboutDialog = true }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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
                    onClick = { showLogoutConfirmDialog = true }
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

    // 1. Theme Dialog
    if (showThemeDialog) {
        val themes = listOf("Light Theme", "Dark Theme", "System Default")
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    themes.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTheme = theme }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedTheme == theme),
                                onClick = { selectedTheme = theme },
                                colors = RadioButtonDefaults.colors(selectedColor = Orange500)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = theme, color = Slate800, fontSize = 15.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mode = com.jobpilot.app.ui.theme.ThemeMode.values().find { it.displayName == selectedTheme }
                            ?: com.jobpilot.app.ui.theme.ThemeMode.SYSTEM
                        com.jobpilot.app.ui.theme.ThemeManager.setThemeMode(mode)
                        showThemeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text("Apply", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        )
    }

    // 2. Change Password Dialog
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (passwordSuccess) {
                        Text("Password changed successfully!", color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                    } else {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm New Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        passwordError?.let {
                            Text(it, color = ErrorRed, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                if (passwordSuccess) {
                    TextButton(onClick = { showPasswordDialog = false }) { Text("Close", color = Orange500) }
                } else {
                    Button(
                        onClick = {
                            if (currentPassword.isBlank() || newPassword.isBlank()) {
                                passwordError = "Please fill in all fields"
                            } else if (newPassword != confirmPassword) {
                                passwordError = "New passwords do not match"
                            } else if (newPassword.length < 8) {
                                passwordError = "Password must be at least 8 characters"
                            } else {
                                passwordError = null
                                passwordSuccess = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Update", color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            },
            dismissButton = {
                if (!passwordSuccess) {
                    TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel", color = Slate600) }
                }
            }
        )
    }

    // 3. Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "JobPilot values your confidentiality.\n\n• Your resumes, academic records, and career goals are strictly stored in encrypted databases.\n• We NEVER sell or share your personal information with third-party advertisers.\n• AI roadmaps and resume scoring are processed with enterprise-grade data isolation.\n• You retain full ownership and can delete your profile data at any time.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate700
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Close", color = Orange500) }
            }
        )
    }

    // 4. Terms Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Terms of Service", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "By utilizing JobPilot, you agree to:\n\n1. Use AI learning roadmaps for legitimate educational and professional development.\n2. Submit accurate personal, educational, and experience records for resume parsing.\n3. Acknowledge that AI career coach suggestions and ATS evaluations are advisory guidelines and do not constitute employment guarantees.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate700
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) { Text("I Agree", color = Orange500) }
            }
        )
    }

    // 5. Contact Support Dialog
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text("Contact Support", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Have questions or encountering an issue?", style = MaterialTheme.typography.bodyMedium, color = Slate800)
                    Text("Email our engineering team:", style = MaterialTheme.typography.bodySmall, color = Slate500)
                    Surface(shape = RoundedCornerShape(8.dp), color = Orange50, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "support@jobpilot.app",
                            color = Orange600,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Text("Response time is typically within 24 hours.", style = MaterialTheme.typography.bodySmall, color = Slate500)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSupportDialog = false }) { Text("OK", color = Orange500) }
            }
        )
    }

    // 6. Open Source Licenses Dialog
    if (showLicensesDialog) {
        AlertDialog(
            onDismissRequest = { showLicensesDialog = false },
            title = { Text("Open Source Licenses", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("JobPilot is powered by modern open source software:", style = MaterialTheme.typography.bodySmall, color = Slate600)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Android Jetpack & Jetpack Compose (Apache 2.0)", fontSize = 12.sp, color = Slate800)
                    Text("• Material Design 3 (Apache 2.0)", fontSize = 12.sp, color = Slate800)
                    Text("• Square Retrofit & OkHttp (Apache 2.0)", fontSize = 12.sp, color = Slate800)
                    Text("• Kotlinx Coroutines & Serialization (Apache 2.0)", fontSize = 12.sp, color = Slate800)
                    Text("• FastAPI & Pydantic (MIT License)", fontSize = 12.sp, color = Slate800)
                    Text("• SQLAlchemy & Alembic (MIT License)", fontSize = 12.sp, color = Slate800)
                }
            },
            confirmButton = {
                TextButton(onClick = { showLicensesDialog = false }) { Text("Close", color = Orange500) }
            }
        )
    }

    // 7. About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About JobPilot", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "JobPilot Android Edition\nVersion 1.0.0 (Release)\n\n\"Your career. Piloted by AI.\"\n\nEngineered with Jetpack Compose, Material 3, and Kotlin Coroutines. Powered by FastAPI and DeepSeek AI on OpenRouter.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate700
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("OK", color = Orange500) }
            }
        )
    }

    // 8. Logout Confirmation Dialog
    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            title = { Text("Log Out?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out of your JobPilot account?", color = Slate700) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        authViewModel.logout(onLogout)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GapOrange)
                ) {
                    Text("Log Out", color = androidx.compose.ui.graphics.Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Cancel", color = Slate600)
                }
            }
        )
    }
}
