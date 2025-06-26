package com.fomaxtro.notemark.data.remote.impl

import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class KtorNoteDataSource(
    private val httpClient: HttpClient
) : NoteDataSource {
    override suspend fun create(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/notes")) {
                setBody(note)
            }
        }
    }

    override suspend fun update(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.put(createRoute("/notes")) {
                setBody(note)
            }
        }
    }

    override suspend fun delete(id: String): EmptyResult<NetworkError> {
        return safeRemoteCall {
            httpClient.delete(createRoute("/notes/$id"))
        }
    }
}