package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.database.entity.UserPreferencesEntity
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.UserPreferences

fun UserPreferencesEntity.toUserPreferences() = UserPreferences(
    syncInterval = SyncInterval.fromInterval(syncInterval)
)

fun UserPreferences.toUserPreferencesEntity(
    userId: String
) = UserPreferencesEntity(
    userId = userId,
    syncInterval = syncInterval.interval
)