package com.soulspace.app.presentation.auth.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = "",
)