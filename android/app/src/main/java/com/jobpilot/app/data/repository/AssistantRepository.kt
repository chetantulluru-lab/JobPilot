package com.jobpilot.app.data.repository

import com.jobpilot.app.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {
    val messagesStream: Flow<List<ChatMessage>>
    suspend fun sendMessage(message: String, jobId: String? = null): Result<ChatMessage>
    suspend fun clearHistory()
}

class MockAssistantRepository : AssistantRepository {
    private val welcomeMessage = com.jobpilot.app.data.model.ChatMessage(
        id = "msg-welcome",
        sender = com.jobpilot.app.data.model.MessageSender.ASSISTANT,
        content = "Hello! I am your JobPilot AI Career Coach. I can analyze your resume, recommend jobs, suggest skills to learn, or help you prepare for upcoming interviews. What would you like to explore today?",
        timestamp = "Just now",
        suggestedActions = listOf(
            "How can I improve my resume match score?",
            "What skills should I learn for Backend roles?",
            "Help me prepare for an upcoming interview"
        )
    )

    private val _messages = kotlinx.coroutines.flow.MutableStateFlow(listOf(welcomeMessage))
    override val messagesStream: Flow<List<ChatMessage>> = _messages

    override suspend fun sendMessage(message: String, jobId: String?): Result<ChatMessage> {
        val userMsg = ChatMessage(
            id = "msg-user-${System.currentTimeMillis()}",
            sender = com.jobpilot.app.data.model.MessageSender.USER,
            content = message,
            timestamp = "Just now"
        )
        val reply = ChatMessage(
            id = "msg-mock-${System.currentTimeMillis()}",
            sender = com.jobpilot.app.data.model.MessageSender.ASSISTANT,
            content = "Based on your verified skills, emphasizing FastAPI, PostgreSQL, and system design in your project bullets will significantly enhance your match score for backend positions.",
            timestamp = "Just now",
            isFallback = true,
            suggestedActions = listOf("Review Profile Skills", "View Job Matches")
        )
        _messages.value = _messages.value + userMsg + reply
        return Result.success(reply)
    }

    override suspend fun clearHistory() {
        _messages.value = listOf(welcomeMessage)
    }
}

