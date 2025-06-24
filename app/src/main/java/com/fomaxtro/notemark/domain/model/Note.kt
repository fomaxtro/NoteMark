package com.fomaxtro.notemark.domain.model

import java.time.Instant
import java.util.UUID

data class Note(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val lastEditedAt: Instant
)
