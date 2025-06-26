package com.fomaxtro.notemark.presentation.screen.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class NoteListViewModel(
    private val defaultTitle: String,
    private val noteRepository: NoteRepository
) : ViewModel() {
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
            val now = Instant.now()

            val note = Note(
                id = UUID.randomUUID(),
                title = defaultTitle,
                content = "",
                createdAt = now,
                lastEditedAt = now
            )

            when (val result = noteRepository.create(note)) {
                is Result.Error -> {
                    eventChannel.send(
                        NoteListEvent.ShowSystemMessage(result.error.toUiText())
                    )
                }
                is Result.Success -> {
                    eventChannel.send(
                        NoteListEvent.NavigateToEditNote(note.id)
                    )
                }
            }
        }
    }
}