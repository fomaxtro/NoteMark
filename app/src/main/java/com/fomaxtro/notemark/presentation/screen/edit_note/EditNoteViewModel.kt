package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.model.UnsavedNote
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.use_case.CheckUnsavedNoteChanges
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
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
    private val noteRepository: NoteRepository,
    private val checkUnsavedNoteChanges: CheckUnsavedNoteChanges
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditNoteState(
            title = TextFieldValue(
                text = defaultTitle,
                selection = TextRange(defaultTitle.length)
            )
        )
    )
    val state = _state
        .onStart { loadNote(id) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            EditNoteState(
                title = TextFieldValue(
                    text = defaultTitle,
                    selection = TextRange(defaultTitle.length)
                )
            )
        )

    private val eventChannel = Channel<EditNoteEvent>()
    val events = eventChannel.receiveAsFlow()

    private lateinit var loadedNote: Note

    private suspend fun loadNote(id: String) {
        loadedNote = noteRepository
            .findById(UUID.fromString(id))
            .first()

        _state.update {
            it.copy(
                title = TextFieldValue(
                    text = loadedNote.title,
                    selection = TextRange(loadedNote.title.length)
                ),
                content = TextFieldState(loadedNote.content)
            )
        }

        eventChannel.send(EditNoteEvent.RequestTitleFocus)
    }

    fun onAction(action: EditNoteAction) {
        when (action) {
            EditNoteAction.OnDiscardClick -> onDiscardClick()
            EditNoteAction.OnDiscardDialogConfirm -> onDiscardDialogConfirm()
            EditNoteAction.OnDiscardDialogDismiss -> onDiscardDialogDismiss()
            EditNoteAction.OnSaveNoteClick -> onSaveNoteClick()
            is EditNoteAction.OnTitleChange -> onTitleChange(action.title)
        }
    }

    private fun onTitleChange(title: TextFieldValue) {
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
                    title = title.text,
                    content = content.text.toString(),
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
            when (
                checkUnsavedNoteChanges(
                    defaultTitle = defaultTitle,
                    currentTitle = state.value.title.text,
                    currentContent = state.value.content.text.toString(),
                    originalNote = loadedNote
                )
            ) {
                UnsavedNote.DELETED,
                UnsavedNote.KEEP_SAVED-> eventChannel.send(EditNoteEvent.NavigateBack)

                UnsavedNote.DISCARDED -> {
                    _state.update {
                        it.copy(
                            showDiscardDialog = true
                        )
                    }
                }
            }
        }
    }
}