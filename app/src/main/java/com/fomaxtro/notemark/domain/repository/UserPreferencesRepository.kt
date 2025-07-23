package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.model.UserPreferences

interface UserPreferencesRepository {
    suspend fun find(): UserPreferences
    suspend fun save(preferences: UserPreferences)
}