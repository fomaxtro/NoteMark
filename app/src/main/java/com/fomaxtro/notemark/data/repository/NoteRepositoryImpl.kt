package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.util.safeDatabaseCall
import com.fomaxtro.notemark.data.mapper.toDataError
import com.fomaxtro.notemark.data.mapper.toNoteDto
import com.fomaxtro.notemark.data.mapper.toNoteEntity
import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.mapError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteDataSource: NoteDataSource,
    private val applicationScope: CoroutineScope
) : NoteRepository {
    override suspend fun createNote(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            noteDataSource.createNote(note.toNoteDto())
        }

        return result.mapError { it.toDataError() }
    }

    override suspend fun updateNote(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            noteDataSource.updateNote(note.toNoteDto())
        }

        return result.mapError { it.toDataError() }
    }
}
