package com.fomaxtro.notemark.presentation.screen.login

sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data object NavigateToRegistration : LoginEvent
}