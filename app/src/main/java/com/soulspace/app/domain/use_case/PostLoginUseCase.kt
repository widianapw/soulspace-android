package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading<AuthResponse>())
            val response = repository.login(email, password)
            emit(Resource.Success<AuthResponse>(response))
        } catch (e: HttpException) {
            emit(Resource.Error<AuthResponse>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<AuthResponse>("Couldn't reach server. Check your internet connection."))
        }
    }
}