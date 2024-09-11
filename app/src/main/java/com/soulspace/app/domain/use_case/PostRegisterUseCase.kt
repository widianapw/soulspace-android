package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRegisterUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        name: String,
    ): Flow<Resource<AuthResponse>>  = flow{
        try {
            emit(Resource.Loading<AuthResponse>())
            val response = repository.register(email, password, name)
            emit(Resource.Success<AuthResponse>(response))
        } catch (e: Exception) {
            emit(Resource.Error<AuthResponse>(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}