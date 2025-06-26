package com.fomaxtro.notemark.presentation.screen.edit_note

import java.util.UUID

data class EditNoteState(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val content: String = ""
)
