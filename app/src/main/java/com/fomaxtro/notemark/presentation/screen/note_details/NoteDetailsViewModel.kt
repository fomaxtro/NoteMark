package com.fomaxtro.notemark.presentation.screen.note_details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NoteDetailsViewModel : ViewModel() {
    private val _state = MutableStateFlow(NoteDetailsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: NoteDetailsAction) {
        when (action) {
            else -> Unit
        }
    }
}