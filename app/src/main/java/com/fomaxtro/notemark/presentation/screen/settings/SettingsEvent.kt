package com.fomaxtro.notemark.presentation.screen.settings

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
}