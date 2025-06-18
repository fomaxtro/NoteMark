package com.fomaxtro.notemark.presentation.screen.landing

sealed interface LandingAction {
    data object OnGetStartedButtonClick : LandingAction
    data object OnLogInButtonClick : LandingAction
}