package com.soulspace.app.presentation.auth.register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulspace.app.common.Resource
import com.soulspace.app.common.TokenManager
import com.soulspace.app.common.UiEvents
import com.soulspace.app.domain.use_case.PostRegisterUseCase
import com.soulspace.app.presentation.auth.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val postRegisterUseCase: PostRegisterUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = mutableStateOf(RegisterState())
    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()
    val state: State<RegisterState> = _state

    fun submitRegister(email: String, password: String, name: String) {
        Log.d("RegisterViewModel", "submitLogin: $email, $password")

        // Reset errors when starting a new login attempt
        _state.value = _state.value.copy(
            isEmailError = false,
            isNameError = false,
            nameErrorMessage = "",
            emailErrorMessage = "",
            isPasswordError = false,
            passwordErrorMessage = "",
            isLoading = false,
            error = ""
        )
        if (name.isEmpty()) {
            _state.value = _state.value.copy(
                isNameError = true,
                nameErrorMessage = "Name is required"
            )
            return
        }

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
            postRegisterUseCase(name = name, email = email, password = password)
                .onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        is Resource.Success -> {
//                            save the token to shared preferences
                            result.data?.data?.let {
                                it.user.let { user ->
                                    tokenManager.saveAuthPrefs(
                                        token = it.accessToken,
                                        userId = user.id,
                                        roomId = user.chatRoom.id
                                    )
                                }
                            }

                            _state.value = _state.value.copy(isLoading = false)
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