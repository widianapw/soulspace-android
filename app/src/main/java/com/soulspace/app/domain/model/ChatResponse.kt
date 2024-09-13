package com.soulspace.app.domain.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    val data: List<ChatResponseItem>,
)

data class ChatResponseItem(
    @SerializedName("chat_room_id")
    val chatRoomId: Int ? =null,
    val id: Int? = null,
    val message: String,
    @SerializedName("sender_type")
    val senderType: String,
    val updated_at: String? = null,
    val created_at: String? = null,
    val deleted_at: Any? = null,
)
