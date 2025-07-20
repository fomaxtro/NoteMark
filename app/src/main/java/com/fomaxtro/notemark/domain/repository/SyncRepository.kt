package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun performFullSync(): Flow<SyncStatus>
}