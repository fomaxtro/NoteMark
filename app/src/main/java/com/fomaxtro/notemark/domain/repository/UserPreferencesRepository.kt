package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun find(): Flow<UserPreferences>
}