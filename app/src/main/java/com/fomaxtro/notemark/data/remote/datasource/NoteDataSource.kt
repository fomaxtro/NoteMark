package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.util.Result

interface NoteDataSource {
    suspend fun createNote(note: NoteDto): Result<NoteDto, NetworkError>
}