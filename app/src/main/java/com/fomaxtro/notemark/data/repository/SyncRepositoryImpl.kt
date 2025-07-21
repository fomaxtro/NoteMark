package com.fomaxtro.notemark.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.fomaxtro.notemark.data.database.dao.SyncInfoDao
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.sync.SyncWorker
import com.fomaxtro.notemark.domain.model.SyncStatus
import com.fomaxtro.notemark.domain.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import java.time.Instant
import java.util.concurrent.TimeUnit

class SyncRepositoryImpl(
    private val context: Context,
    private val syncInfoDao: SyncInfoDao,
    private val sessionStorage: SecureSessionStorage
) : SyncRepository {
    override fun performFullSync(): Flow<SyncStatus> = flow {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            )
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniqueWork(
            "sync",
            ExistingWorkPolicy.REPLACE,
            syncWorkRequest
        ).await()

        emitAll(
            workManager
                .getWorkInfoByIdFlow(syncWorkRequest.id)
                .map { workInfo ->
                    when (workInfo?.state) {
                        WorkInfo.State.RUNNING -> SyncStatus.SYNCING

                        WorkInfo.State.ENQUEUED,
                        WorkInfo.State.BLOCKED -> null

                        WorkInfo.State.SUCCEEDED -> SyncStatus.SYNCED

                        else -> SyncStatus.FAILED
                    }
                }
                .filterNotNull()
                .takeWhile { syncStatus ->
                    emit(syncStatus)

                    syncStatus == SyncStatus.SYNCING
                }
        )
    }

    override fun getLastSyncTime(): Flow<Instant?> = flow {
        val userId = sessionStorage.getUserId() ?: return@flow emit(null)

        emitAll(
            syncInfoDao.findByUserId(userId)
                .map { syncInfo ->
                    syncInfo?.let {
                        Instant.ofEpochMilli(it.lastSyncTime)
                    }
                }
        )
    }
}