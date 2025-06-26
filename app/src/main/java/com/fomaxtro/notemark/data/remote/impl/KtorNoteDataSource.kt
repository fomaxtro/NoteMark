package com.fomaxtro.notemark.data.remote.impl

import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class KtorNoteDataSource(
    private val httpClient: HttpClient
) : NoteDataSource {
    override suspend fun createNote(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/notes")) {
                setBody(note)
            }
        }
    }

    override suspend fun updateNote(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.put(createRoute("/notes")) {
                setBody(note)
            }
        }
    }
}