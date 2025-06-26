package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.NoteEntity

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsert(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun findById(id: String): NoteEntity

    @Delete
    suspend fun delete(note: NoteEntity)
}