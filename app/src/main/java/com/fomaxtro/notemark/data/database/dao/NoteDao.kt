package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.fomaxtro.notemark.data.database.entity.NoteEntity

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)
}