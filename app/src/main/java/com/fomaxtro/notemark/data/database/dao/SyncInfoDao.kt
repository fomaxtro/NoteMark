package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.SyncInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncInfoDao {
    @Query("SELECT * FROM sync_info WHERE user_id = :userId LIMIT 1")
    fun findByUserId(userId: String): Flow<SyncInfoEntity?>

    @Upsert
    suspend fun upsert(syncInfo: SyncInfoEntity)
}