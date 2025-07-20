package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.database.entity.NoteEntity
import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.domain.model.Note
import java.time.Instant
import java.util.UUID

fun Note.toNoteEntity() = NoteEntity(
    id = id.toString(),
    title = title,
    content = content,
    createdAt = createdAt.toEpochMilli(),
    lastEditedAt = lastEditedAt.toEpochMilli()
)

fun Note.toNoteDto() = NoteDto(
    id = id.toString(),
    title = title,
    content = content,
    createdAt = createdAt.toString(),
    lastEditedAt = lastEditedAt.toString()

)

fun NoteEntity.toNote() = Note(
    id = UUID.fromString(id),
    title = title,
    content = content,
    createdAt = Instant.ofEpochMilli(createdAt),
    lastEditedAt = Instant.ofEpochMilli(lastEditedAt)
)

fun NoteEntity.toNoteDto() = NoteDto(
    id = id,
    title = title,
    content = content,
    createdAt = Instant.ofEpochMilli(createdAt).toString(),
    lastEditedAt = Instant.ofEpochMilli(lastEditedAt).toString()
)
