package com.fomaxtro.notemark.presentation.model

import java.util.UUID

data class NoteUi(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: String
)
