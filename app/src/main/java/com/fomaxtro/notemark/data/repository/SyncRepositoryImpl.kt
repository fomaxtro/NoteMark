package com.fomaxtro.notemark.data.repository

import android.content.Context
import androidx.lifecycle.Observer
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.fomaxtro.notemark.data.sync.SyncWorker
import com.fomaxtro.notemark.domain.model.SyncStatus
import com.fomaxtro.notemark.domain.repository.SyncRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class SyncRepositoryImpl(
    private val context: Context
) : SyncRepository {
    override fun performFullSync(): Flow<SyncStatus> = callbackFlow {
        trySend(SyncStatus.SYNCING)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                SyncWorker.MIN_BACKOFF_SECONDS,
                TimeUnit.SECONDS
            )
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniqueWork(
            "sync",
            ExistingWorkPolicy.REPLACE,
            syncWorkRequest
        ).await()

        val workInfoLiveData = workManager
            .getWorkInfoByIdLiveData(syncWorkRequest.id)

        val workInfoObserver = Observer<WorkInfo?> { workInfo ->
            when (workInfo?.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING,
                WorkInfo.State.BLOCKED -> trySend(SyncStatus.SYNCING)

                WorkInfo.State.SUCCEEDED -> {
                    trySend(SyncStatus.SYNCED)
                    close()
                }

                else -> {
                    trySend(SyncStatus.FAILED)
                    close()
                }
            }
        }

        workInfoLiveData.observeForever(workInfoObserver)

        awaitClose {
            workInfoLiveData.removeObserver(workInfoObserver)
        }
    }
}