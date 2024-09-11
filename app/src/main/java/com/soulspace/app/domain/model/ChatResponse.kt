package com.soulspace.app.domain.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    val data: List<ChatResponseItem>,
)

data class ChatResponseItem(
    @SerializedName("chat_room_id")
    val chatRoomId: String,
    val id: Int,
    val message: String,
    @SerializedName("sender_type")
    val senderType: String,
    val updated_at: String,
    val created_at: String,
    val deleted_at: Any,
)
