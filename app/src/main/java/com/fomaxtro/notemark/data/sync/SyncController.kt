package com.fomaxtro.notemark.data.sync

import com.fomaxtro.notemark.data.database.dao.SyncDao
import com.fomaxtro.notemark.data.database.entity.NoteEntity
import com.fomaxtro.notemark.data.database.entity.SyncEntity
import com.fomaxtro.notemark.data.database.entity.SyncOperation
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage

class SyncController(
    private val syncDao: SyncDao,
    private val sessionStorage: SecureSessionStorage
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
}