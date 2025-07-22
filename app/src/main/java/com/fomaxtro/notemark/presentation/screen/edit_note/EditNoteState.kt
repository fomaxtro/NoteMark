package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.ui.text.input.TextFieldValue
import com.fomaxtro.notemark.domain.model.Note

data class EditNoteState(
    val title: TextFieldValue = TextFieldValue(),
    val content: String = "",
    val showDiscardDialog: Boolean = false,
    val loadedNote: Note? = null,
) {
    fun requireLoadedNote(): Note = requireNotNull(loadedNote)
}
