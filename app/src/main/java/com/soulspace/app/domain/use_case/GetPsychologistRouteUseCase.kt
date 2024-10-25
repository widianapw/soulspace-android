package com.soulspace.app.domain.use_case

import com.soulspace.app.common.Resource
import com.soulspace.app.domain.model.ChatResponse
import com.soulspace.app.domain.model.NavigationItem
import com.soulspace.app.domain.repository.SoulSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPsychologistRouteUseCase @Inject constructor(
    private val repository: SoulSpaceRepository
) {
    operator fun invoke(
        fromLatitude: Double? = null,
        fromLongitude: Double? = null,
        toLatitude: Double? = null,
        toLongitude: Double? = null
    ): Flow<Resource<List<NavigationItem>>> = flow {
        try {
            emit(Resource.Loading<List<NavigationItem>>())
            val response = repository.getPsychologistRoute(
                fromLatitude, fromLongitude, toLatitude, toLongitude
            )
            emit(Resource.Success<List<NavigationItem>>(response))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<NavigationItem>>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        }
    }
}