package com.fomaxtro.notemark.presentation.screen.registration

import androidx.compose.foundation.text.input.TextFieldState
import com.fomaxtro.notemark.presentation.ui.UiText

data class RegistrationState(
    val username: String = "",
    val isFocusedUsername: Boolean = false,
    val usernameHint: UiText? = null,
    val isUsernameError: Boolean = false,
    val usernameError: UiText? = null,

    val email: String = "",
    val isFocusedEmail: Boolean = false,
    val isEmailError: Boolean = false,
    val emailError: UiText? = null,

    val password: TextFieldState = TextFieldState(),
    val isFocusedPassword: Boolean = false,
    val passwordHint: UiText? = null,
    val isPasswordError: Boolean = false,
    val passwordError: UiText? = null,
    val showPassword: Boolean = false,

    val passwordConfirmation: TextFieldState = TextFieldState(),
    val isFocusedPasswordConfirmation: Boolean = false,
    val isPasswordConfirmationError: Boolean = false,
    val passwordConfirmationError: UiText? = null,
    val showPasswordConfirmation: Boolean = false,

    val canCreateAccount: Boolean = false,
    val isLoading: Boolean = false
)
