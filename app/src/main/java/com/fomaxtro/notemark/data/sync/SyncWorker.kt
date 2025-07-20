package com.fomaxtro.notemark.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(
    private val syncController: SyncController,
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private companion object {
        const val MAX_ATTEMPTS = 3
    }

    override suspend fun doWork(): Result {
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

        return Result.success()
    }
}