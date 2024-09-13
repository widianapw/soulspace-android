package com.soulspace.app.presentation.psychologist.psychologist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.soulspace.app.common.Resource
import com.soulspace.app.domain.use_case.GetPsychologistUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.soulspace.app.common.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PsychologistViewModel @Inject constructor(
    private val getPsychologistUseCase: GetPsychologistUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = mutableStateOf(PsychologistState())

    val state: State<PsychologistState> = _state

    init {
        getPsychologists()
    }

    private fun getPsychologists() {
        getPsychologistUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = PsychologistState(list = it.data?.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = PsychologistState(error = it.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = PsychologistState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout(){
        tokenManager.clearAuthPrefs()
    }
}