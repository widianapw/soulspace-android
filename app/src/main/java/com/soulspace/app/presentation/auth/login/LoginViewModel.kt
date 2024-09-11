package com.soulspace.app.presentation.auth.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulspace.app.common.Resource
import com.soulspace.app.common.UiEvents
import com.soulspace.app.domain.use_case.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
) : ViewModel() {
    private val _state = mutableStateOf(LoginState())
    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()
    val state: State<LoginState> = _state

    fun submitLogin(email: String, password: String) {
        Log.d("LoginViewModel", "submitLogin: $email, $password")

        // Reset errors when starting a new login attempt
        _state.value = _state.value.copy(
            isEmailError = false,
            emailErrorMessage = "",
            isPasswordError = false,
            passwordErrorMessage = "",
            isLoading = false,
            isSuccess = false,
            error = ""
        )

        if (email.isEmpty()) {
            _state.value = _state.value.copy(
                isEmailError = true,
                emailErrorMessage = "Email is required"
            )
            return
        }

        if (password.isEmpty()) {
            _state.value = _state.value.copy(
                isPasswordError = true,
                passwordErrorMessage = "Password is required"
            )
            return
        }

        // Perform login operation inside a coroutine
        viewModelScope.launch {
            postLoginUseCase(email, password)
                .onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                            _eventFlow.emit(
                                UiEvents.SuccessEvent()
                            )
                        }

                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = result.message ?: "An unexpected error occurred"
                            )
                            _eventFlow.emit(
                                UiEvents.SnackbarEvent(
                                    result.message ?: "An unexpected error occurred"
                                )
                            )
                        }
                    }
                }
                .launchIn(this) // Collect the flow in the current coroutine scope
        }
    }
}