package com.soulspace.app.domain.repository

import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.model.PsychologistResponse


interface SoulSpaceRepository {
    suspend fun login(email: String, password: String): AuthResponse
    suspend fun register(name: String, email: String, password: String): AuthResponse
    suspend fun getPsychologists(): PsychologistResponse
    suspend fun getChat(): ChatResponse
    suspend fun sendMessage(message: String): PostChatResponse
}