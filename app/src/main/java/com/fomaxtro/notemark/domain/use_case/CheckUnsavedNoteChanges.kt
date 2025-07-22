package com.fomaxtro.notemark.domain.use_case

import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.model.UnsavedNoteAction

class CheckUnsavedNoteChanges {
    operator fun invoke(
        defaultTitle: String,
        currentTitle: String,
        currentContent: String,
        originalNote: Note
    ): UnsavedNoteAction {
        return when {
            currentTitle == defaultTitle
                    && currentTitle == originalNote.title
                    && currentContent.isEmpty() -> {
                UnsavedNoteAction.DELETE
            }

            currentTitle != originalNote.title || currentContent != originalNote.content -> {
                UnsavedNoteAction.DISCARD
            }

            else -> UnsavedNoteAction.KEEP_SAVED
        }
    }
}