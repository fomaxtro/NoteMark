package com.fomaxtro.notemark.presentation.screen.edit_note

sealed interface EditNoteAction {
    data class OnTitleChange(val title: String) : EditNoteAction
    data class OnContentChange(val content: String) : EditNoteAction
    data object OnDiscardClick : EditNoteAction
    data object OnDiscardDialogDismiss : EditNoteAction
    data object OnDiscardDialogConfirm : EditNoteAction
    data object OnSaveNoteClick : EditNoteAction
}