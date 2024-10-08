package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.AuthResponse
import com.soulspace.app.domain.model.PsychologistResponse
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPsychologistUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(
        latitude: Double? = null,
        longitude: Double? = null
    ): Flow<Resource<PsychologistResponse>> = flow {
        try {
            emit(Resource.Loading<PsychologistResponse>())
            val response = repository.getPsychologists(latitude, longitude)
            emit(Resource.Success<PsychologistResponse>(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error<PsychologistResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<PsychologistResponse>("Couldn't reach server. Check your internet connection."))
        }

    }
}