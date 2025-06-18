package com.fomaxtro.notemark.presentation.screen.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoading: Boolean = false
)
