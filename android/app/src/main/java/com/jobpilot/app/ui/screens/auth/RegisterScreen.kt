package com.jobpilot.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var keystone by remember { mutableStateOf("") }
    var showOtpDialog by remember { mutableStateOf(false) }
    var enteredOtp by remember { mutableStateOf("") }
    var useOtpMode by remember { mutableStateOf(true) }

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
            text = "Create Your Profile",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Begin your AI-piloted career journey",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Toggle between Email OTP and Quick Keystone
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate100, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { useOtpMode = true },
                shape = RoundedCornerShape(10.dp),
                color = if (useOtpMode) BgWhite else Slate100,
                shadowElevation = if (useOtpMode) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.MarkEmailRead,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (useOtpMode) Orange500 else Slate500
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Email OTP",
                        fontSize = 13.sp,
                        fontWeight = if (useOtpMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (useOtpMode) Slate900 else Slate500
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { useOtpMode = false },
                shape = RoundedCornerShape(10.dp),
                color = if (!useOtpMode) BgWhite else Slate100,
                shadowElevation = if (!useOtpMode) 2.dp else 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.VpnKey,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (!useOtpMode) Orange500 else Slate500
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Quick Sign Up",
                        fontSize = 13.sp,
                        fontWeight = if (!useOtpMode) FontWeight.Bold else FontWeight.Medium,
                        color = if (!useOtpMode) Slate900 else Slate500
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BgWhite
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("College / Personal Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password (min 6 characters)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = JobPilotShapes.medium,
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                if (!useOtpMode) {
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = keystone,
                        onValueChange = { keystone = it },
                        label = { Text("Security Keystone (Secret Recovery Word)") },
                        placeholder = { Text("e.g. secret word, nickname, or pet") },
                        supportingText = {
                            Text(
                                text = "Used to reset password if you ever forget it. Save this word!",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = JobPilotShapes.medium,
                        singleLine = true
                    )
                }

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (useOtpMode) {
                    JobPilotButton(
                        text = if (uiState.isLoading) "Sending OTP Code..." else "Send Email Verification OTP",
                        onClick = {
                            viewModel.startRegistration(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password,
                                onOtpSent = {
                                    showOtpDialog = true
                                }
                            )
                        },
                        enabled = !uiState.isLoading &&
                            fullName.isNotBlank() &&
                            email.isNotBlank() &&
                            email.contains("@") &&
                            password.length >= 6
                    )
                } else {
                    JobPilotButton(
                        text = if (uiState.isLoading) "Creating Account..." else "Create Account",
                        onClick = {
                            viewModel.register(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password,
                                keystone = keystone.trim().ifBlank { "jobpilot" },
                                onSuccess = onRegisterSuccess
                            )
                        },
                        enabled = !uiState.isLoading &&
                            fullName.isNotBlank() &&
                            email.isNotBlank() &&
                            password.length >= 6 &&
                            keystone.trim().length >= 3
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

    // OTP Verification Dialog
    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = {
                Text(
                    text = "Verify Your Email",
                    style = MaterialTheme.typography.titleLarge,
                    color = Slate900,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "We sent a 6-digit verification code to:\n$email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate700
                    )

                    if (uiState.successMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Orange50,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = uiState.successMessage ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = Orange600,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { if (it.length <= 6) enteredOtp = it.filter { char -> char.isDigit() } },
                        label = { Text("6-Digit Code") },
                        placeholder = { Text("123456") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    TextButton(
                        onClick = {
                            viewModel.startRegistration(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password,
                                onOtpSent = {}
                            )
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Resend Code", color = Orange500, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.verifyRegistrationOtp(
                            email = email.trim(),
                            otp = enteredOtp.trim(),
                            onSuccess = {
                                showOtpDialog = false
                                onRegisterSuccess()
                            }
                        )
                    },
                    enabled = !uiState.isLoading && enteredOtp.length == 6,
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                ) {
                    Text(if (uiState.isLoading) "Verifying..." else "Verify & Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) {
                    Text("Cancel", color = Slate500)
                }
            }
        )
    }
}
