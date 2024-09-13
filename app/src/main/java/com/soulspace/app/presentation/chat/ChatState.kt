package com.soulspace.app.presentation.chat

import com.soulspace.app.domain.model.ChatResponseItem

data class ChatState(
    val messages: List<ChatResponseItem> = emptyList(),
    val message: String = "",
    val isLoading: Boolean = false,
    val isLoadingSendMessage: Boolean = false,
    val error: String = ""
)
