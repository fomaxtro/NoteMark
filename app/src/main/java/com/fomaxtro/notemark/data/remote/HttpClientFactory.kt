package com.fomaxtro.notemark.data.remote

import com.fomaxtro.notemark.BuildConfig
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.remote.dto.RefreshTokenRequest
import com.fomaxtro.notemark.data.remote.dto.RefreshTokenResponse
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

object HttpClientFactory {
    fun create(
        sessionStorage: SecureSessionStorage
    ): HttpClient {
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

                if (BuildConfig.API_DEBUG) {
                    header("Debug", true)
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenPair = sessionStorage.getTokenPair()

                        tokenPair?.let {
                            BearerTokens(
                                accessToken = it.accessToken,
                                refreshToken = it.refreshToken
                            )
                        }
                    }

                    refreshTokens {
                        val tokenPair = sessionStorage
                            .getTokenPair() ?: return@refreshTokens null

                        val response = safeRemoteCall<RefreshTokenResponse> {
                            client.post(createRoute("/auth/refresh")) {
                                setBody(
                                    RefreshTokenRequest(
                                        refreshToken = tokenPair.refreshToken
                                    )
                                )

                                markAsRefreshTokenRequest()
                            }
                        }

                        when (response) {
                            is Result.Error -> null
                            is Result.Success -> {
                                sessionStorage.saveTokenPair(
                                    TokenPair(
                                        accessToken = response.data.accessToken,
                                        refreshToken = response.data.refreshToken
                                    )
                                )

                                BearerTokens(
                                    accessToken = response.data.accessToken,
                                    refreshToken = response.data.refreshToken
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}