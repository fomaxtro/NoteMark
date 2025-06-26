package com.fomaxtro.notemark.presentation.screen.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteListViewModel : ViewModel() {
    private val _state = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteListEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.OnNewNoteClick -> onNewNoteClick()
        }
    }

    private fun onNewNoteClick() {
        viewModelScope.launch {
            eventChannel.send(NoteListEvent.NavigateToEditNote)
        }
    }
}