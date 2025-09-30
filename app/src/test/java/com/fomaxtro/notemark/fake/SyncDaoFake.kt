package com.fomaxtro.notemark.fake

import com.fomaxtro.notemark.data.database.dao.SyncDao
import com.fomaxtro.notemark.data.database.entity.SyncEntity
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.withContext

class SyncDaoFake(
    private val dispatcher: TestDispatcher
) : SyncDao {
    val syncEntities = mutableListOf<SyncEntity>()

    override suspend fun findByUserIdAndNoteId(
        userId: String,
        noteId: String
    ): SyncEntity? {
        return withContext(dispatcher) {
            syncEntities.find { it.userId == userId && it.note.id == noteId }
        }
    }

    override suspend fun findByUserId(userId: String): List<SyncEntity> {
        return withContext(dispatcher) {
            syncEntities.filter { it.userId == userId }
        }
    }

    override suspend fun upsert(sync: SyncEntity) {
        withContext(dispatcher) {
            val index = syncEntities.indexOfFirst { it.id == sync.id }

            if (index != -1) {
                syncEntities[index] = sync
            } else {
                syncEntities.add(sync)
            }
        }
    }

    override suspend fun delete(sync: SyncEntity) {
        withContext(dispatcher) {
            syncEntities.remove(sync)
        }
    }
}