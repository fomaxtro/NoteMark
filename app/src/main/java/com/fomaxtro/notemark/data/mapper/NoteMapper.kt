package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.database.entity.NoteEntity
import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.domain.model.Note

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