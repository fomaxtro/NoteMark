package com.fomaxtro.notemark.util

import com.fomaxtro.notemark.domain.model.Note
import java.time.Instant
import java.util.UUID

object NoteFactory {
    fun create(
        id: UUID = UUID.randomUUID(),
        title: String = "default",
        content: String = "",
        createdAt: Instant = Instant.now(),
        lastEditedAt: Instant = Instant.now()
    ) = Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )
}