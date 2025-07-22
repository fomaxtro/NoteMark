package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE user_id = :userId LIMIT 1")
    fun findByUserId(userId: String): Flow<UserPreferencesEntity?>

    @Upsert
    suspend fun upsert(syncInfo: UserPreferencesEntity)
}