package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.ResetChatResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetChatUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(): Flow<Resource<ResetChatResponse>> = flow {
        try {
            emit(Resource.Loading<ResetChatResponse>())
            val response = repository.resetChat()
            emit(Resource.Success<ResetChatResponse>(response))
        } catch (e: Exception) {
            emit(
                Resource.Error<ResetChatResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        }
    }

}