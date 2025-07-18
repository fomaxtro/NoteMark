package com.fomaxtro.notemark.presentation.screen.registration

import androidx.compose.foundation.text.input.TextFieldState
import com.fomaxtro.notemark.presentation.ui.UiText

data class RegistrationState(
    val username: String = "",
    val isUsernameFocused: Boolean = false,
    val isUsernameError: Boolean = false,
    val usernameError: UiText? = null,

    val email: String = "",
    val isEmailFocused: Boolean = false,
    val isEmailError: Boolean = false,
    val emailError: UiText? = null,

    val password: TextFieldState = TextFieldState(),
    val isPasswordFocused: Boolean = false,
    val isPasswordError: Boolean = false,
    val passwordError: UiText? = null,

    val passwordConfirmation: TextFieldState = TextFieldState(),
    val isPasswordConfirmationFocused: Boolean = false,
    val isPasswordConfirmationError: Boolean = false,
    val passwordConfirmationError: UiText? = null,

    val canCreateAccount: Boolean = false,
    val isLoading: Boolean = false
)
