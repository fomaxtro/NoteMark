package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface NoteRepository {
    suspend fun create(note: Note): EmptyResult<DataError>
    suspend fun update(note: Note): EmptyResult<DataError>
    suspend fun findById(id: UUID): Note
    suspend fun delete(noteId: UUID)
    fun getRecentNotes(): Flow<List<Note>>
}