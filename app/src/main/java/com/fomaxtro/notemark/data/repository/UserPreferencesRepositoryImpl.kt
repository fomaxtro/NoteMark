package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.UserPreferencesDao
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.mapper.toUserPreferences
import com.fomaxtro.notemark.data.mapper.toUserPreferencesEntity
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.UserPreferences
import com.fomaxtro.notemark.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first

class UserPreferencesRepositoryImpl(
    private val sessionStorage: SecureSessionStorage,
    private val userPreferencesDao: UserPreferencesDao
) : UserPreferencesRepository {
    override suspend fun find(): UserPreferences {
        val userId = requireNotNull(sessionStorage.getUserId())
        val userPreferencesEntity = userPreferencesDao.findByUserId(userId)
            .first()

        return userPreferencesEntity?.toUserPreferences() ?: UserPreferences(
            syncInterval = SyncInterval.MANUAL_ONLY
        )
    }

    override suspend fun save(preferences: UserPreferences) {
        val userId = requireNotNull(sessionStorage.getUserId())

        userPreferencesDao.upsert(preferences.toUserPreferencesEntity(userId))
    }
}