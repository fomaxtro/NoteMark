package com.fomaxtro.notemark.presentation.screen.edit_note

data class EditNoteState(
    val title: String = "",
    val content: String = "",
    val showDiscardDialog: Boolean = false
)
