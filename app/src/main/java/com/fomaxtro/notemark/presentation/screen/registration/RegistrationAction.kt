package com.fomaxtro.notemark.presentation.screen.registration

sealed interface RegistrationAction {
    data class OnUsernameChange(val username: String) : RegistrationAction
    data class OnUsernameFocusChange(val isFocused: Boolean) : RegistrationAction
    data class OnEmailChange(val email: String) : RegistrationAction
    data class OnEmailFocusChange(val isFocused: Boolean) : RegistrationAction
    data class OnPasswordFocusChange(val isFocused: Boolean) : RegistrationAction
    data class OnPasswordConfirmationFocusChange(val isFocused: Boolean) : RegistrationAction
    data object OnAlreadyHaveAccountClick : RegistrationAction
    data object OnCreateAccountClick : RegistrationAction
}