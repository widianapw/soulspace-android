package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.PostChatResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostSendMessageUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(
        message: String
    ): Flow<Resource<PostChatResponse>> = flow {
        try {
            emit(Resource.Loading<PostChatResponse>())
            val response = repository.sendMessage(message)
            emit(Resource.Success<PostChatResponse>(response))
        } catch (e: Exception) {
            emit(
                Resource.Error<PostChatResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        }
    }
}