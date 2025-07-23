package com.fomaxtro.notemark.presentation.screen.settings

import com.fomaxtro.notemark.domain.model.SyncInterval

sealed interface SettingsAction {
    data object OnNavigateBackClick : SettingsAction
    data object OnLogoutClick : SettingsAction
    data object OnSyncDataClick : SettingsAction
    data class OnSyncIntervalChange(val interval: SyncInterval) : SettingsAction
}