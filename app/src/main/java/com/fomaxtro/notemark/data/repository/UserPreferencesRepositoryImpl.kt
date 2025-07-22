package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.UserPreferencesDao
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.mapper.toUserPreferences
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.UserPreferences
import com.fomaxtro.notemark.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class UserPreferencesRepositoryImpl(
    private val sessionStorage: SecureSessionStorage,
    private val userPreferencesDao: UserPreferencesDao
) : UserPreferencesRepository {
    override fun find(): Flow<UserPreferences> = flow {
        val userId = requireNotNull(sessionStorage.getUserId())

        emitAll(
            userPreferencesDao
                .findByUserId(userId)
                .map { userPreferencesEntity ->
                    userPreferencesEntity?.toUserPreferences() ?: UserPreferences(
                        userId = UUID.fromString(userId),
                        syncInterval = SyncInterval.MANUAL_ONLY
                    )
                }
        )
    }
}