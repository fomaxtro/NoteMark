package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.NoteEntity

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsert(note: NoteEntity)
}