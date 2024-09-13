package com.soulspace.app.presentation.auth.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isNameError: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val nameErrorMessage: String = "",
    val emailErrorMessage: String = "",
    val passwordErrorMessage: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)