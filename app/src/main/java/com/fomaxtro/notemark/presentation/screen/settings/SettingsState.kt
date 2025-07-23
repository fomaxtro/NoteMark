package com.fomaxtro.notemark.presentation.screen.settings

import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.presentation.ui.UiText

data class SettingsState(
    val isLoggingOut: Boolean = false,
    val isSyncing: Boolean = false,
    val lastSyncTime: UiText = UiText.DynamicString(""),
    val hasInternetConnection: Boolean = true,
    val syncInterval: SyncInterval = SyncInterval.MANUAL_ONLY
)
