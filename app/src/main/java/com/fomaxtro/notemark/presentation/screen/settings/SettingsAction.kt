package com.fomaxtro.notemark.presentation.screen.settings

sealed interface SettingsAction {
    data object OnNavigateBackClick : SettingsAction
    data object OnLogoutClick : SettingsAction
    data object OnSyncDataClick : SettingsAction
}