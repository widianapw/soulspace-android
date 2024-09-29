package com.soulspace.app.domain.repository

import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.model.PsychologistResponse
import com.soulspace.app.domain.model.ResetChatResponse


interface SoulSpaceRepository {
    suspend fun login(email: String, password: String): AuthResponse
    suspend fun register(name: String, email: String, password: String): AuthResponse
    suspend fun getPsychologists(
        latitude: Double? = null,
        longitude: Double? = null
    ): PsychologistResponse

    suspend fun getChat(): ChatResponse
    suspend fun sendMessage(message: String): PostChatResponse
    suspend fun resetChat(): ResetChatResponse
}