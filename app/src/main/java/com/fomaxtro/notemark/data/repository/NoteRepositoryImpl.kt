package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.entity.SyncOperation
import com.fomaxtro.notemark.data.database.util.safeDatabaseCall
import com.fomaxtro.notemark.data.mapper.toDataError
import com.fomaxtro.notemark.data.mapper.toNote
import com.fomaxtro.notemark.data.mapper.toNoteEntity
import com.fomaxtro.notemark.data.sync.SyncController
import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.mapError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val applicationScope: CoroutineScope,
    private val syncController: SyncController
) : NoteRepository {
    override suspend fun create(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.INSERT)
        }

        return result.mapError { it.toDataError() }
    }

    override suspend fun update(note: Note): EmptyResult<DataError> {
        val result = safeDatabaseCall {
            noteDao.upsert(note.toNoteEntity())
        }

        applicationScope.launch {
            syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.UPDATE)
        }

        return result.mapError { it.toDataError() }
    }

    override fun findById(id: UUID): Flow<Note> {
        return noteDao
            .findById(id.toString())
            .map { it.toNote() }
    }

    override suspend fun delete(noteId: UUID) {
        val noteEntity = noteDao.findById(noteId.toString())
            .first()

        applicationScope.launch {
            syncController.scheduleSyncOperation(noteEntity, SyncOperation.DELETE)
        }

        noteDao.delete(noteEntity)
    }

    override fun getRecentNotes(): Flow<List<Note>> {
        return noteDao
            .getRecentNotes()
            .map { notes ->
                notes.map { it.toNote() }
            }
    }
}
