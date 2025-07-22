package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.model.UnsavedNoteAction
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.use_case.CheckUnsavedNoteChanges
import com.fomaxtro.notemark.presentation.screen.edit_note.mode.EditNoteMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class EditNoteViewModel(
    id: String,
    private val mode: EditNoteMode,
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
        .onStart {
            loadNote(id)

            if (mode == EditNoteMode.EDIT) {
                observeFieldChanges()
            }
        }
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

    @OptIn(FlowPreview::class)
    private fun observeFieldChanges() {
        state
            .filter { it.loadedNote != null }
            .map { state ->
                Triple(
                    state.title.text,
                    state.content,
                    state.requireLoadedNote()
                )
            }
            .distinctUntilChanged { old, new ->
                old.first == new.first && old.second == new.second
            }
            .debounce(1000)
            .onEach { (title, content, loadedNote) ->
                val note = loadedNote.copy(
                    title = title,
                    content = content
                )

                noteRepository.update(note)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun loadNote(id: String) {
        val loadedNote = noteRepository
            .findById(UUID.fromString(id))
            .first()

        _state.update {
            it.copy(
                loadedNote = loadedNote,
                title = TextFieldValue(
                    text = loadedNote.title,
                    selection = TextRange(loadedNote.title.length)
                ),
                content = loadedNote.content
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
            is EditNoteAction.OnContentChange -> onContentChange(action.content)
        }
    }

    private fun onContentChange(content: String) {
        _state.update {
            it.copy(
                content = content
            )
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
                val loadedNote = requireLoadedNote()

                Note(
                    id = loadedNote.id,
                    title = title.text,
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

    private fun handleDiscardOnCreate() {
        viewModelScope.launch {
            when (
                checkUnsavedNoteChanges(
                    defaultTitle = defaultTitle,
                    currentTitle = state.value.title.text,
                    currentContent = state.value.content,
                    originalNote = state.value.requireLoadedNote()
                )
            ) {
                UnsavedNoteAction.DELETE -> {
                    noteRepository.delete(state.value.requireLoadedNote().id)
                    eventChannel.send(EditNoteEvent.NavigateBack)
                }

                UnsavedNoteAction.KEEP_SAVED-> eventChannel.send(EditNoteEvent.NavigateBack)

                UnsavedNoteAction.DISCARD -> {
                    _state.update {
                        it.copy(
                            showDiscardDialog = true
                        )
                    }
                }
            }
        }
    }

    private fun handleDiscardOnEdit() {
        viewModelScope.launch {
            val note = with(state.value) {
                requireLoadedNote().copy(
                    title = title.text,
                    content = content,
                    lastEditedAt = Instant.now()
                )
            }

            noteRepository.update(note)
            eventChannel.send(EditNoteEvent.NavigateBack)
        }
    }

    private fun onDiscardClick() {
        when (mode) {
            EditNoteMode.CREATE -> handleDiscardOnCreate()
            EditNoteMode.EDIT -> handleDiscardOnEdit()
        }
    }
}