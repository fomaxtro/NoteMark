package com.fomaxtro.notemark.presentation.screen.note_details

sealed interface NoteDetailsAction {
    data object OnNavigateBackClick : NoteDetailsAction
}