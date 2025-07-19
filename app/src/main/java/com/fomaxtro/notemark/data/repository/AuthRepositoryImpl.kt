package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.NoteMarkDatabase
import com.fomaxtro.notemark.data.datastore.SessionStorage
import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.mapper.toLoginError
import com.fomaxtro.notemark.data.remote.datasource.AuthRemoteDataSource
import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.LogoutRequest
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.util.asEmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class AuthRepositoryImpl(
    private val authDataSource: AuthRemoteDataSource,
    private val sessionStorage: SessionStorage,
    private val database: NoteMarkDatabase
) : AuthRepository {
    override suspend fun login(email: String, password: String): EmptyResult<LoginError> {
        val result =  authDataSource
            .login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

        return when (result) {
            is Result.Error -> Result.Error(result.error.toLoginError())
            is Result.Success -> {
                with(result.data) {
                    sessionStorage.saveAuthInfo(
                        AuthInfo(
                            id = UUID.nameUUIDFromBytes(email.encodeToByteArray()).toString(),
                            username = username,
                            tokenPair = TokenPair(
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        )
                    )
                }

                result.asEmptyResult()
            }
        }
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return sessionStorage
            .getAuthInfo()
            .map { it != null }
    }

    override suspend fun logout() {
        // TODO: Clear note table only
        /*withContext(Dispatchers.IO) {
            database.clearAllTables()
        }*/

        val tokenPair = sessionStorage.getTokenPair()

        tokenPair?.let { tokenPair ->
            authDataSource.logout(
                LogoutRequest(
                    refreshToken = tokenPair.refreshToken
                )
            )
        }

        sessionStorage.removeAuthInfo()
    }
}