package com.jobpilot.app.ui.screens.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.model.ChatMessage
import com.jobpilot.app.data.model.MessageSender
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.AssistantViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AICareerAssistantScreen(
    viewModel: AssistantViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll to bottom when a new message arrives
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    val starterPrompts = listOf(
        "Analyze my profile for Python backend roles",
        "How do I bridge missing skills for high match jobs?",
        "Help me draft a concise message to a recruiter",
        "Generate 3 mock interview questions for FastAPI"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Career Coach",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.textPrimary
                        )
                        Text(
                            text = "Zero-Fabrication • Grounded in Your Profile",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = Orange500
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Slate800
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Chat",
                            tint = Slate500
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgWarmWhite)
            )
        },
        bottomBar = {
            Surface(
                color = BgWarmWhite,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Quick Prompt Chips
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(starterPrompts) { prompt ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = BgWhite,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                                modifier = Modifier.clickable {
                                    viewModel.sendMessage(prompt)
                                }
                            ) {
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 12.sp,
                                    color = Slate700,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Chat Input Bar
                    Surface(
                        color = BgWhite,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = uiState.inputText,
                                onValueChange = { viewModel.onInputChanged(it) },
                                placeholder = {
                                    Text(
                                        text = "Ask your AI Coach anything...",
                                        fontSize = 14.sp,
                                        color = Slate400
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Orange500,
                                    unfocusedBorderColor = Slate300
                                ),
                                maxLines = 4
                            )

                            IconButton(
                                onClick = {
                                    viewModel.sendMessage()
                                    coroutineScope.launch {
                                        if (uiState.messages.isNotEmpty()) {
                                            listState.animateScrollToItem(uiState.messages.size - 1)
                                        }
                                    }
                                },
                                enabled = uiState.inputText.isNotBlank() && !uiState.isSending,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (uiState.inputText.isNotBlank() && !uiState.isSending) Orange500 else Slate200)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = if (uiState.inputText.isNotBlank() && !uiState.isSending) Color.White else Slate400
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Safety & Grounding Banner
            Surface(
                color = Orange50,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Orange600,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Answers cite only your confirmed experience and skills to ensure authentic career progression.",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = Slate700
                    )
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(uiState.messages) { msg ->
                    MessageBubble(
                        message = msg,
                        onSuggestedActionClick = { prompt ->
                            viewModel.sendMessage(prompt)
                        }
                    )
                }

                if (uiState.isSending) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            AIOrb(size = 28.dp, showRings = false)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "AI Coach is formulating grounded guidance...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    onSuggestedActionClick: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            AIOrb(size = 32.dp, showRings = false)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) Orange500 else MaterialTheme.cardBg,
                border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.cardBorder),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isUser) Color.White else MaterialTheme.textPrimary,
                        lineHeight = 20.sp
                    )

                    if (!isUser && message.isFallback) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "⚡ Grounded Fallback Mode",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.textMuted
                        )
                    }
                }
            }

            if (!isUser && message.suggestedActions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    message.suggestedActions.forEach { action ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Orange50,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Orange200
                            ),
                            modifier = Modifier.clickable { onSuggestedActionClick(action) }
                        ) {
                            Text(
                                text = "✦ $action",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = Orange500,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
