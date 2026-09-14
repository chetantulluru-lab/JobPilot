package com.jobpilot.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.jobpilot.app.data.model.AccountProvider
import com.jobpilot.app.data.model.EmailCategory
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.SectionHeader
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.ConnectedAccountsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedAccountsScreen(
    viewModel: ConnectedAccountsViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Connected Accounts", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Google / Gmail OAuth 2.0 Integration Highlight
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    val gmail = uiState.accounts.find { it.provider == AccountProvider.GOOGLE_GMAIL }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = null,
                                    tint = Orange500,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Google / Gmail Integration",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Slate900
                                    )
                                    Text(
                                        text = if (gmail?.isConnected == true) "Gmail Connected ✓" else "Not Connected",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (gmail?.isConnected == true) SuccessGreen else Slate500,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                    if (gmail?.isConnected == true) {
                        OutlinedButton(
                            onClick = { viewModel.disconnect(AccountProvider.GOOGLE_GMAIL) },
                            shape = ButtonShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Disconnect", color = Slate600, fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.startOAuthConnect(AccountProvider.GOOGLE_GMAIL) { url ->
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                            shape = ButtonShape,
                            enabled = !uiState.isConnectingGoogle,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (uiState.isConnectingGoogle) "Authorizing..." else "Connect Gmail",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // OAuth 2.0 Security Guarantee Note
                Surface(
                    shape = JobPilotShapes.small,
                    color = Orange50,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔒 Security Notice: JobPilot connects exclusively via official Google OAuth 2.0 tokens. We never prompt for, see, or store your Google or Gmail password.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Orange700,
                        modifier = Modifier.padding(10.dp),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    // GitHub Integration
    item {
        val github = uiState.accounts.find { it.provider == AccountProvider.GITHUB }
        GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "GitHub Developer Account", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (github?.isConnected == true) "Connected: ${github.accountEmailOrHandle ?: "Verified"}" else "Connect to verify repositories",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (github?.isConnected == true) SuccessGreen else Slate500
                    )
                }

                if (github?.isConnected == true) {
                    OutlinedButton(onClick = { viewModel.disconnect(AccountProvider.GITHUB) }, shape = ButtonShape) {
                        Text("Unlink", color = Slate600)
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.startOAuthConnect(AccountProvider.GITHUB) { url ->
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                context.startActivity(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                        shape = ButtonShape
                    ) {
                        Text("Connect", color = Color.White)
                    }
                }
            }
        }
    }

    // LinkedIn Integration
    item {
        val linkedin = uiState.accounts.find { it.provider == AccountProvider.LINKEDIN }
        GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "LinkedIn Professional Profile", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = if (linkedin?.isConnected == true) "Connected: ${linkedin.accountEmailOrHandle ?: "Verified"}" else "Connect to sync profile",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (linkedin?.isConnected == true) SuccessGreen else Slate500
                    )
                }

                if (linkedin?.isConnected == true) {
                    OutlinedButton(onClick = { viewModel.disconnect(AccountProvider.LINKEDIN) }, shape = ButtonShape) {
                        Text("Unlink", color = Slate600)
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.startOAuthConnect(AccountProvider.LINKEDIN) { url ->
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                context.startActivity(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = InfoBlue),
                        shape = ButtonShape
                    ) {
                        Text("Connect", color = Color.White)
                    }
                }
            }
        }
    }

    // Email Classification Stream
    item {
        SectionHeader(
            title = "NLP Email Classification Architecture",
            subtitle = "Automated reply and interview detection"
        )
    }

    if (uiState.emailEvents.isEmpty()) {
        item {
            GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No synced application emails",
                        style = MaterialTheme.typography.titleMedium,
                        color = Slate700
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect Gmail to automatically track recruiter replies and interview schedules.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
        }
    } else {
        items(uiState.emailEvents) { mail ->
            GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = BgWhite) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = mail.senderName, style = MaterialTheme.typography.titleMedium, color = Slate900)
                        Surface(shape = ChipShape, color = Orange50) {
                            Text(
                                text = mail.category.displayName,
                                color = Orange600,
                                style = JobPilotTypography.labelMedium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(text = mail.subject, style = MaterialTheme.typography.bodySmall, color = Slate700, fontWeight = FontWeight.SemiBold)
                    Text(text = mail.snippet, style = MaterialTheme.typography.bodySmall, color = Slate500)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Classified with ${(mail.confidenceScore * 100).toInt()}% NLP confidence • ${mail.receivedDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

            if (uiState.feedbackMessage != null) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = SuccessGreenBg) {
                        Text(text = uiState.feedbackMessage ?: "", color = SuccessGreen, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
