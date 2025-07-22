package com.fomaxtro.notemark.domain.model

import java.util.UUID

data class UserPreferences(
    val userId: UUID,
    val syncInterval: SyncInterval
)
