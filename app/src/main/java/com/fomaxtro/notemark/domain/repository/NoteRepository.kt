package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.util.EmptyResult

interface NoteRepository {
    suspend fun createNote(note: Note): EmptyResult<DataError>
}