package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.SyncEntity

@Dao
interface SyncDao {
    @Query("SELECT * FROM syncs WHERE user_id = :userId AND local_id = :noteId LIMIT 1")
    suspend fun findByUserIdAndNoteId(userId: String, noteId: String): SyncEntity? 

    @Upsert
    suspend fun upsert(sync: SyncEntity)

    @Delete
    suspend fun delete(sync: SyncEntity)
}