package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.ui.text.input.TextFieldValue

sealed interface EditNoteAction {
    data class OnTitleChange(val title: TextFieldValue) : EditNoteAction
    data object OnDiscardClick : EditNoteAction
    data object OnDiscardDialogDismiss : EditNoteAction
    data object OnDiscardDialogConfirm : EditNoteAction
    data object OnSaveNoteClick : EditNoteAction
}