package com.fomaxtro.notemark.presentation.mapper

import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.presentation.model.NoteUi
import com.fomaxtro.notemark.presentation.util.toScopedDate
import java.time.ZoneOffset

fun Note.toNoteUi() = NoteUi(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt
        .atZone(ZoneOffset.UTC)
        .toScopedDate()
)