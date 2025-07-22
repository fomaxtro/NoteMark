package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.database.entity.UserPreferencesEntity
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.UserPreferences
import java.util.UUID

fun UserPreferencesEntity.toUserPreferences() = UserPreferences(
    userId = UUID.fromString(userId),
    syncInterval = SyncInterval.fromInterval(syncInterval)
)