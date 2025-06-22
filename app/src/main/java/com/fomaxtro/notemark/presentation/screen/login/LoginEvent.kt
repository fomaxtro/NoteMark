package com.fomaxtro.notemark.presentation.screen.login

import com.fomaxtro.notemark.presentation.ui.UiText

sealed interface LoginEvent {
    data object NavigateToNoteList : LoginEvent
    data object NavigateToRegistration : LoginEvent
    data class ShowMessage(val message: UiText) : LoginEvent
}