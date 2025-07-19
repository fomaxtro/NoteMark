package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.SyncInfoEntity

@Dao
interface SyncInfoDao {
    @Query("SELECT * FROM sync_info WHERE user_id = :userId LIMIT 1")
    suspend fun findByUserId(userId: String): SyncInfoEntity?

    @Upsert
    suspend fun upsert(syncInfo: SyncInfoEntity)
}