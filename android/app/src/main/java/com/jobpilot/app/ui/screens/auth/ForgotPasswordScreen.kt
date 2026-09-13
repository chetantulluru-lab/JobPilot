package com.jobpilot.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("chetan@example.com") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AIOrb(size = 80.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Enter your registered email to receive recovery instructions",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    singleLine = true
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (uiState.successMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.successMessage ?: "",
                        color = SuccessGreen,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                JobPilotButton(
                    text = if (uiState.isLoading) "Sending..." else "Send Recovery Link",
                    onClick = {
                        viewModel.forgotPassword(email)
                    },
                    enabled = !uiState.isLoading
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "← Back to Sign In",
            style = JobPilotTypography.labelLarge,
            color = Orange500,
            modifier = Modifier.clickable { onNavigateBack() }
        )
    }
}
