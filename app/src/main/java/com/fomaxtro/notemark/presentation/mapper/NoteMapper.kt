package com.fomaxtro.notemark.presentation.mapper

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.presentation.screen.note_details.model.NoteDetailUi
import com.fomaxtro.notemark.presentation.screen.note_list.model.NoteUi
import com.fomaxtro.notemark.presentation.ui.UiText
import com.fomaxtro.notemark.presentation.util.toScopedDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun Note.toNoteUi() = NoteUi(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt
        .atZone(ZoneId.systemDefault())
        .toScopedDate()
)

fun Note.toNoteDetailUi(): NoteDetailUi {
    val now = Instant.now()
    val timeDiff = lastEditedAt.until(now, ChronoUnit.MINUTES)
    val dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    return NoteDetailUi(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt
            .atZone(ZoneId.systemDefault())
            .format(dateFormat),
        lastEditedAt = if (timeDiff < 5) {
            UiText.StringResource(R.string.just_now)
        } else {
            val lastEditedAtFormatted = lastEditedAt
                .atZone(ZoneId.systemDefault())
                .format(dateFormat)

            UiText.DynamicString(lastEditedAtFormatted)
        }
    )
}