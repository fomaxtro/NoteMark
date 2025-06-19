package com.fomaxtro.notemark.presentation.screen.registration

import com.fomaxtro.notemark.presentation.ui.UiText

sealed interface RegistrationEvent {
    data object NavigateToLogin : RegistrationEvent
    data class ShowMessage(val message: UiText) : RegistrationEvent
}