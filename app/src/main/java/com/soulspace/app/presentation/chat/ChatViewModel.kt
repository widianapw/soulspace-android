package com.soulspace.app.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulspace.app.common.Resource
import com.soulspace.app.common.UiEvents
import com.soulspace.app.domain.use_case.GetChatUseCase
import com.soulspace.app.domain.use_case.GetPsychologistUseCase
import com.soulspace.app.domain.use_case.PostSendMessageUseCase
import com.soulspace.app.domain.use_case.ResetChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatUseCase: GetChatUseCase,
    private val postSendMessageUseCase: PostSendMessageUseCase,
    private val resetChatUseCase: ResetChatUseCase
) : ViewModel() {
    val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state
    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getChat()
    }

    fun getChat(showLoading: Boolean = true) {
        getChatUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(messages = result.data?.data ?: emptyList(), isLoading = false)
                }

                is Resource.Error -> {

                    _state.value =
                        _state.value.copy(error = result.message ?: "An unexpected error occurred", isLoading = false)
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "An unexpected error occurred"
                        )
                    )
                }

                is Resource.Loading -> {
                    if(showLoading){
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setMessage(message: String){
        _state.value = _state.value.copy(message = message)
    }

    fun postMessage(message: String){
        postSendMessageUseCase(message).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getChat(
                        showLoading = false
                    )
                    _state.value = _state.value.copy(message = "", isLoadingSendMessage = false)
                }

                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "An unexpected error occurred"
                        )
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoadingSendMessage = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetChat(){
        resetChatUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getChat()
                }

                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "An unexpected error occurred"
                        )
                    )
                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}