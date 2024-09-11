package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke() : Flow<Resource<ChatResponse>> = flow {
        try {
            emit(Resource.Loading<ChatResponse>())
            val response = repository.getChat()
            emit(Resource.Success<ChatResponse>(response))
        } catch (e: Exception) {
            emit(Resource.Error<ChatResponse>(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}