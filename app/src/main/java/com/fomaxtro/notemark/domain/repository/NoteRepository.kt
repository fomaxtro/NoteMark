package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.model.Note

interface NoteRepository {
    suspend fun createNote(note: Note)
}