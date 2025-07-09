package com.fomaxtro.notemark.presentation.screen.note_list

import com.fomaxtro.notemark.presentation.ui.UiText

sealed interface NoteListEvent {
    data class NavigateToEditNote(val id: String) : NoteListEvent
    data class ShowSystemMessage(val message: UiText) : NoteListEvent
    data object NavigateToSettings : NoteListEvent
    data class NavigateToNoteDetails(val id: String) : NoteListEvent
}