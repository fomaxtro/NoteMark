package com.fomaxtro.notemark.data.datastore.store

import androidx.datastore.core.Serializer
import com.fomaxtro.notemark.data.crypto.Crypto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object EncryptedPreferenceSerializer : Serializer<SecurePreference> {
    override val defaultValue: SecurePreference
        get() = SecurePreference()

    override suspend fun readFrom(input: InputStream): SecurePreference {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }

        val decryptedBytes = Crypto.decrypt(encryptedBytes)

        return try {
            Json.decodeFromString(decryptedBytes.decodeToString())
        } catch (e: Exception) {
            e.printStackTrace()

            if (e is CancellationException) throw e

            defaultValue
        }
    }

    override suspend fun writeTo(t: SecurePreference, output: OutputStream) {
        val jsonEncoded = Json.encodeToString(t)
        val encryptedBytes = Crypto.encrypt(jsonEncoded.toByteArray())

        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytes)
            }
        }
    }
}