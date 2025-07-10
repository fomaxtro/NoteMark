package com.fomaxtro.notemark.presentation.screen.note_details

sealed interface NoteDetailsAction {
    data object OnNavigateBackClick : NoteDetailsAction
    data object OnEditNoteClick : NoteDetailsAction
    data class OnReaderModeClick(val readerMode: Boolean) : NoteDetailsAction
    data object OnTapScreen : NoteDetailsAction
    data object OnContentScroll : NoteDetailsAction
}