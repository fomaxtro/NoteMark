package com.fomaxtro.notemark.data.remote

import com.fomaxtro.notemark.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                level = LogLevel.BODY

                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization
                }

                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.v(message)
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.USER_EMAIL)
            }
        }
    }
}