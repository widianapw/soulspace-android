package com.soulspace.app.data.repository

import com.soulspace.app.data.remote.SoulSpaceApi
import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.model.PsychologistResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import javax.inject.Inject

class SoulSpaceRepositoryImpl @Inject constructor(
    private val soulSpaceApi: SoulSpaceApi
) : SoulSpaceRepository {

    override suspend fun login(email: String, password: String): AuthResponse {
        return soulSpaceApi.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): AuthResponse {
        return soulSpaceApi.register(name, email, password)
    }

    override suspend fun getPsychologists(): PsychologistResponse {
        return soulSpaceApi.getPsychologists()
    }

    override suspend fun getChat(): ChatResponse {
        return soulSpaceApi.getChat()
    }

    override suspend fun sendMessage(message: String): PostChatResponse {
        return soulSpaceApi.sendMessage(message)
    }

}