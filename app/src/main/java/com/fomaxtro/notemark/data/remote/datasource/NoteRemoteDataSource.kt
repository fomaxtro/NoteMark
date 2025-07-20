package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.NoteDto
import com.fomaxtro.notemark.data.remote.dto.NotesResponse
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class NoteRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun create(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/notes")) {
                setBody(note)
            }
        }
    }

    suspend fun update(note: NoteDto): Result<NoteDto, NetworkError> {
        return safeRemoteCall {
            httpClient.put(createRoute("/notes")) {
                setBody(note)
            }
        }
    }

    suspend fun delete(id: String): EmptyResult<NetworkError> {
        return safeRemoteCall {
            httpClient.delete(createRoute("/notes/$id"))
        }
    }

    suspend fun getAll(): Result<NotesResponse, NetworkError> {
        return safeRemoteCall {
            httpClient.get(createRoute("/notes")) {
                parameter("page", -1)
                parameter("size", -1)
            }
        }
    }
}