package com.jobpilot.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isOtpStep by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AIOrb(size = 80.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isOtpStep) "Verify Your Email" else "Create Your Profile",
            style = MaterialTheme.typography.headlineLarge,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isOtpStep) "Enter the 6-digit verification code sent to $email" else "Begin your AI-piloted career journey",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!isOtpStep) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("College / Personal Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password (min 6 characters)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                } else {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 6) otpCode = it },
                        label = { Text("6-Digit Verification Code") },
                        placeholder = { Text("123456") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextButton(
                        onClick = { isOtpStep = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Edit registration details", color = Orange600, style = MaterialTheme.typography.bodySmall)
                    }
                }

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (uiState.successMessage != null && isOtpStep) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.successMessage ?: "",
                        color = SuccessGreen,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isOtpStep) {
                    JobPilotButton(
                        text = if (uiState.isLoading) "Sending code..." else "Send Verification Code",
                        onClick = {
                            if (fullName.isNotBlank() && email.isNotBlank() && password.length >= 6) {
                                viewModel.startRegistration(fullName.trim(), email.trim(), password) {
                                    isOtpStep = true
                                }
                            } else {
                                viewModel.register(fullName.trim(), email.trim(), password, onRegisterSuccess)
                            }
                        },
                        enabled = !uiState.isLoading && fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()
                    )
                } else {
                    JobPilotButton(
                        text = if (uiState.isLoading) "Verifying..." else "Verify & Create Account",
                        onClick = {
                            viewModel.verifyRegistrationOtp(email.trim(), otpCode.trim(), onRegisterSuccess)
                        },
                        enabled = !uiState.isLoading && otpCode.length >= 6
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate600
            )
            Text(
                text = "Sign In",
                style = JobPilotTypography.labelLarge,
                color = Orange500,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}
