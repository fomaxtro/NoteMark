package com.fomaxtro.notemark.data.sync

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.dao.SyncDao
import com.fomaxtro.notemark.data.database.entity.NoteEntity
import com.fomaxtro.notemark.data.database.entity.SyncEntity
import com.fomaxtro.notemark.data.database.entity.SyncOperation
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.mapper.toNoteDto
import com.fomaxtro.notemark.data.remote.datasource.NoteRemoteDataSource
import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.util.asEmptyResult
import java.time.Instant

class SyncController(
    private val syncDao: SyncDao,
    private val sessionStorage: SecureSessionStorage,
    private val noteDataSource: NoteRemoteDataSource,
    private val noteDao: NoteDao
) {
    suspend fun scheduleSyncOperation(note: NoteEntity, operation: SyncOperation) {
        val userId = sessionStorage.getUserId() ?: return
        val scheduledNote = syncDao.findByUserIdAndNoteId(
            userId = userId,
            noteId = note.id
        )

        if (scheduledNote == null) {
            syncDao.upsert(
                SyncEntity(
                    userId = userId,
                    operation = operation,
                    note = note
                )
            )

            return
        }

        when (scheduledNote.operation) {
            SyncOperation.INSERT -> {
                when (operation) {
                    SyncOperation.UPDATE -> {
                        val syncNote = scheduledNote.copy(
                            note = note
                        )

                        syncDao.upsert(syncNote)
                    }

                    SyncOperation.DELETE -> {
                        syncDao.delete(scheduledNote)
                    }

                    SyncOperation.INSERT -> return
                }
            }

            SyncOperation.UPDATE -> {
                when (operation) {
                    SyncOperation.UPDATE -> {
                        val syncNote = scheduledNote.copy(
                            note = note
                        )

                        syncDao.upsert(syncNote)
                    }

                    SyncOperation.DELETE -> {
                        syncDao.delete(scheduledNote)
                        syncDao.upsert(
                            SyncEntity(
                                userId = userId,
                                operation = operation,
                                note = note
                            )
                        )
                    }

                    SyncOperation.INSERT -> return
                }
            }

            SyncOperation.DELETE -> return
        }
    }

    suspend fun syncPendingOperations(): EmptyResult<SyncError> {
        val userId = sessionStorage.getUserId() ?: return Result.Error(SyncError.UNAUTHORIZED)

        val remoteNotes = when (val notesResult = noteDataSource.getAll()) {
            is Result.Error -> return Result.Error(SyncError.FAILED_TO_FETCH_NOTES)
            is Result.Success -> notesResult.data.notes
        }

        val pendingSyncOperations = syncDao.findByUserId(userId)

        pendingSyncOperations.forEach { syncOperation ->
            when (syncOperation.operation) {
                SyncOperation.INSERT -> {
                    val result = noteDataSource.create(syncOperation.note.toNoteDto())

                    if (result is Result.Error) {
                        return Result.Error(SyncError.FAILED_TO_CREATE_NOTE)
                    }
                }

                SyncOperation.UPDATE -> {
                    val remoteNote = remoteNotes.findNoteByNoteId(syncOperation.note.id)

                    if (remoteNote != null) {
                        val remoteResult = pushUpdateToRemote(
                            remoteNote = remoteNote,
                            localNote = syncOperation.note
                        )

                        if (remoteResult is Result.Error) {
                            return Result.Error(SyncError.FAILED_TO_UPDATE_NOTE)
                        }
                    }
                }

                SyncOperation.DELETE -> {
                    val remoteNote = remoteNotes.findNoteByNoteId(syncOperation.note.id)

                    if (remoteNote != null) {
                        val remoteResult = noteDataSource.delete(syncOperation.note.id)

                        if (remoteResult is Result.Error) {
                            return Result.Error(SyncError.FAILED_TO_DELETE_NOTE)
                        }
                    }
                }
            }

            syncDao.delete(syncOperation)
        }

        return Result.Success(Unit)
    }

    private fun List<NoteDto>.findNoteByNoteId(noteId: String): NoteDto? {
        return find { note ->
            note.id == noteId
        }
    }

    private suspend fun pushUpdateToRemote(
        remoteNote: NoteDto,
        localNote: NoteEntity
    ): EmptyResult<NetworkError> {
        val remoteLastEditedAt = Instant
            .parse(remoteNote.lastEditedAt)
            .toEpochMilli()

        return if (localNote.lastEditedAt > remoteLastEditedAt) {
            noteDataSource.update(localNote.toNoteDto())
                .asEmptyResult()
        } else Result.Success(Unit)
    }
}