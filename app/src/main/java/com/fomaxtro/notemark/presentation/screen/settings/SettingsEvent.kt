package com.fomaxtro.notemark.presentation.screen.settings

import com.fomaxtro.notemark.presentation.ui.UiText

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data class ShoSystemMessage(val message: UiText) : SettingsEvent
    data class ShowMessage(val message: UiText) : SettingsEvent
}