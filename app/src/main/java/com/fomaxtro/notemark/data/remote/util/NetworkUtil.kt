package com.fomaxtro.notemark.data.remote.util

import com.fomaxtro.notemark.BuildConfig
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import timber.log.Timber

suspend inline fun <reified T> safeRemoteCall(
    call: () -> HttpResponse
): Result<T, NetworkError> {
    return try {
        responseToResult(call())
    } catch (e: UnresolvedAddressException) {
        Timber.e(e)

        Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        Timber.e(e)

        Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e

        Timber.e(e)

        Result.Error(NetworkError.UNKNOWN)
    }
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body())
        400 -> Result.Error(NetworkError.BAD_REQUEST)
        401 -> Result.Error(NetworkError.UNAUTHORIZED)
        404 -> Result.Error(NetworkError.NOT_FOUND)
        409 -> Result.Error(NetworkError.CONFLICT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUEST)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}

fun createRoute(route: String): String {
    return when {
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}
