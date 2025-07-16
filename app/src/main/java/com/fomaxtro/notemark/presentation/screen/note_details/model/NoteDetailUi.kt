package com.fomaxtro.notemark.presentation.screen.note_details.model

import com.fomaxtro.notemark.presentation.ui.UiText
import java.util.UUID

data class NoteDetailUi(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: UiText
)