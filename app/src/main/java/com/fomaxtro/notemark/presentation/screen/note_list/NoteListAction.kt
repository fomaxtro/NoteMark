package com.fomaxtro.notemark.presentation.screen.note_list

import com.fomaxtro.notemark.presentation.screen.note_list.model.NoteUi

sealed interface NoteListAction {
    data object OnNewNoteClick : NoteListAction
    data class OnNoteClick(val note: NoteUi) : NoteListAction
    data class OnNoteLongClick(val note: NoteUi) : NoteListAction
    data object OnDeleteNoteDialogDismiss : NoteListAction
    data object OnDeleteNoteDialogConfirm : NoteListAction
    data object OnSettingsClick : NoteListAction
}