package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.input.TextFieldValue

data class EditNoteState(
    val title: TextFieldValue = TextFieldValue(),
    val content: TextFieldState = TextFieldState(),
    val showDiscardDialog: Boolean = false
)
