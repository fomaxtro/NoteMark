package com.fomaxtro.notemark.presentation.screen.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val email: String = "",
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoading: Boolean = false
)
