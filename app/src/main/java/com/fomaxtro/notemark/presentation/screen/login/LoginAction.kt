package com.fomaxtro.notemark.presentation.screen.login

sealed interface LoginAction {
    data class OnEmailChange(val email: String) : LoginAction
    data object OnLogInClick : LoginAction
    data object OnDontHaveAccountClick : LoginAction
}