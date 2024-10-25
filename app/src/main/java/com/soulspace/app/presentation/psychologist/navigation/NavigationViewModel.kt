package com.soulspace.app.presentation.psychologist.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulspace.app.common.Resource
import com.soulspace.app.domain.use_case.GetPsychologistRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getPsychologistRouteUseCase: GetPsychologistRouteUseCase
) : ViewModel() {
    private val _state = mutableStateOf(NavigationState())

    val state: State<NavigationState> = _state

    fun getRoute(
        fromLatitude: Double? = null,
        fromLongitude: Double? = null,
        toLatitude: Double? = null,
        toLongitude: Double? = null
    ) {
        getPsychologistRouteUseCase(
            fromLatitude, fromLongitude, toLatitude, toLongitude
        ).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = NavigationState(list = it.data ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value =
                        NavigationState(error = it.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = NavigationState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}