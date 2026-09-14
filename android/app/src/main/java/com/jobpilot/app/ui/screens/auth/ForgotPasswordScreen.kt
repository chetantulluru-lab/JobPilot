package com.jobpilot.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(1) } // 1: Request OTP, 2: Verify & Reset

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

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
            text = if (step == 1) "Reset Password" else "Enter Verification Code",
            style = MaterialTheme.typography.headlineLarge,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (step == 1)
                "Enter your registered email to receive a 6-digit verification code"
            else
                "We sent a 6-digit code to $email",
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
                if (step == 1) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it.trim() },
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

                    Spacer(modifier = Modifier.height(24.dp))

                    JobPilotButton(
                        text = if (uiState.isLoading) "Sending code..." else "Send Verification Code",
                        onClick = {
                            if (email.isNotBlank() && email.contains("@")) {
                                viewModel.startForgotPassword(email) {
                                    step = 2
                                }
                            }
                        },
                        enabled = !uiState.isLoading && email.isNotBlank()
                    )
                } else {
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it.trim() },
                        label = { Text("6-Digit OTP Code") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password (min 6 chars)") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
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
                        text = if (uiState.isLoading) "Updating Password..." else "Reset Password",
                        onClick = {
                            if (otp.length == 6 && newPassword.length >= 6) {
                                viewModel.verifyForgotPassword(email, otp, newPassword) {
                                    onNavigateBack()
                                }
                            }
                        },
                        enabled = !uiState.isLoading && otp.length == 6 && newPassword.length >= 6
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            viewModel.startForgotPassword(email) {}
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Resend Code",
                            color = Orange500,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
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
