package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class EditNoteViewModel(
    id: String,
    private val defaultTitle: String,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditNoteState(
            title = defaultTitle
        )
    )
    val state = _state
        .onStart { loadNote(id) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            EditNoteState(
                title = defaultTitle
            )
        )

    private val eventChannel = Channel<EditNoteEvent>()
    val events = eventChannel.receiveAsFlow()

    private lateinit var loadedNote: Note

    private suspend fun loadNote(id: String) {
        loadedNote = noteRepository.findById(UUID.fromString(id))

        _state.update {
            it.copy(
                title = loadedNote.title,
                content = loadedNote.content
            )
        }
    }

    fun onAction(action: EditNoteAction) {
        when (action) {
            is EditNoteAction.OnContentChange -> onContentChange(action.content)
            EditNoteAction.OnDiscardClick -> onDiscardClick()
            EditNoteAction.OnDiscardDialogConfirm -> onDiscardDialogConfirm()
            EditNoteAction.OnDiscardDialogDismiss -> onDiscardDialogDismiss()
            EditNoteAction.OnSaveNoteClick -> onSaveNoteClick()
            is EditNoteAction.OnTitleChange -> onTitleChange(action.title)
        }
    }

    private fun onTitleChange(title: String) {
        _state.update {
            it.copy(
                title = title
            )
        }
    }

    private fun onSaveNoteClick() {
        viewModelScope.launch {
            val note = with(state.value) {
                Note(
                    id = loadedNote.id,
                    title = title,
                    content = content,
                    createdAt = loadedNote.createdAt,
                    lastEditedAt = Instant.now()
                )
            }

            noteRepository.update(note)
            eventChannel.send(EditNoteEvent.NavigateBack)
        }
    }

    private fun onDiscardDialogDismiss() {
        _state.update {
            it.copy(
                showDiscardDialog = false
            )
        }
    }

    private fun onDiscardDialogConfirm() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showDiscardDialog = false
                )
            }

            delay(100)

            eventChannel.send(EditNoteEvent.NavigateBack)
        }
    }

    private fun onDiscardClick() {
        viewModelScope.launch {
            if (
                state.value.title == defaultTitle
                    && state.value.title == loadedNote.title
                    && state.value.content.isEmpty()
            ) {
                noteRepository.delete(loadedNote)

                eventChannel.send(EditNoteEvent.NavigateBack)
            } else if (
                state.value.title != loadedNote.title || state.value.content != loadedNote.content
            ) {
                _state.update {
                    it.copy(
                        showDiscardDialog = true
                    )
                }
            } else {
                eventChannel.send(EditNoteEvent.NavigateBack)
            }
        }
    }

    private fun onContentChange(content: String) {
        _state.update {
            it.copy(
                content = content
            )
        }
    }
}