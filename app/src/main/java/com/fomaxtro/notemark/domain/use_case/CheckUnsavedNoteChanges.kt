package com.fomaxtro.notemark.domain.use_case

import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.model.UnsavedNote
import com.fomaxtro.notemark.domain.repository.NoteRepository

class CheckUnsavedNoteChanges(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(
        defaultTitle: String,
        currentTitle: String,
        currentContent: String,
        originalNote: Note
    ): UnsavedNote {
        if (currentTitle == defaultTitle && currentTitle == originalNote.title && currentContent.isEmpty()) {
            noteRepository.delete(originalNote.id)

            return UnsavedNote.DELETED
        } else if (currentTitle != originalNote.title || currentContent != originalNote.content) {
            return UnsavedNote.DISCARDED
        } else {
            return UnsavedNote.KEEP_SAVED
        }
    }
}