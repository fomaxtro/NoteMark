package com.fomaxtro.notemark.presentation.screen.landing

sealed interface LandingEvent {
    data object NavigateToRegistration : LandingEvent
    data object NavigateToLogin : LandingEvent
}