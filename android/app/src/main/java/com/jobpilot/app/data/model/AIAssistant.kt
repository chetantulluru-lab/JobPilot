package com.jobpilot.app.data.model

enum class MessageSender {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val content: String,
    val timestamp: String,
    val isFallback: Boolean = false,
    val suggestedActions: List<String> = emptyList()
)

data class Conversation(
    val id: String,
    val title: String,
    val contextType: String,
    val messages: List<ChatMessage> = emptyList()
)
