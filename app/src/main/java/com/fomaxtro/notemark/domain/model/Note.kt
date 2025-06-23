package com.fomaxtro.notemark.domain.model

import java.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Note(
    val id: Uuid,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val lastEditedAt: Instant
)
