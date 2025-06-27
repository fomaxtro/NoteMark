package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.util.safeDatabaseCall
import com.fomaxtro.notemark.data.mapper.toDataError
import com.fomaxtro.notemark.data.mapper.toNote
import com.fomaxtro.notemark.data.mapper.toNoteDto
import com.fomaxtro.notemark.data.mapper.toNoteEntity
import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.mapError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteDataSource: NoteDataSource,
    private val applicationScope: CoroutineScope
) : NoteRepository {
    override suspend fun create(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            noteDataSource.create(note.toNoteDto())
        }

        return result.mapError { it.toDataError() }
    }

    override suspend fun update(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            noteDataSource.update(note.toNoteDto())
        }

        return result.mapError { it.toDataError() }
    }

    override suspend fun findById(id: UUID): Note {
        return noteDao
            .findById(id.toString())
            .toNote()
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note.toNoteEntity())

        applicationScope.launch {
            noteDataSource.delete(note.id.toString())
        }
    }

    override fun getRecentNotes(): Flow<List<Note>> {
        return noteDao
            .getRecentNotes()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }
}
