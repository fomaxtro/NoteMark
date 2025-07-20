package com.fomaxtro.notemark.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fomaxtro.notemark.data.database.dao.SyncInfoDao
import com.fomaxtro.notemark.data.database.entity.SyncInfoEntity
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage

class SyncWorker(
    private val syncController: SyncController,
    private val syncInfoDao: SyncInfoDao,
    private val sessionStorage: SecureSessionStorage,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    companion object {
        const val MAX_ATTEMPTS = 3
        const val MIN_BACKOFF_SECONDS = 30L
    }

    override suspend fun doWork(): Result {
        val userId = sessionStorage.getUserId() ?: return Result.failure()

        if (runAttemptCount > MAX_ATTEMPTS - 1) {
            return Result.failure()
        }

        val syncResult = syncController.syncPendingOperations()

        if (syncResult is com.fomaxtro.notemark.domain.util.Result.Error) {
            return Result.retry()
        }

        val pullResult = syncController.pullAndMergeFromRemote()

        if (pullResult is com.fomaxtro.notemark.domain.util.Result.Error) {
            return Result.retry()
        }

        syncInfoDao.upsert(
            SyncInfoEntity(
                userId = userId,
                lastSyncTime = System.currentTimeMillis()
            )
        )

        return Result.success()
    }
}