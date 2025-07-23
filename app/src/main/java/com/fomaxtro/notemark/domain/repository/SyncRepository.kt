package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface SyncRepository {
    fun performFullSync(): Flow<SyncStatus>
    fun getLastSyncTime(): Flow<Instant?>
    fun schedulePeriodicSync(interval: SyncInterval)
    fun cancelPeriodicSync()
}