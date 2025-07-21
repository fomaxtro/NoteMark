package com.fomaxtro.notemark.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fomaxtro.notemark.data.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsert(note: NoteEntity)

    @Upsert
    suspend fun upsert(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun findById(id: String): Flow<NoteEntity>

    @Delete
    suspend fun delete(note: NoteEntity)

    @Delete
    suspend fun delete(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    fun getRecentNotes(): Flow<List<NoteEntity>>

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}