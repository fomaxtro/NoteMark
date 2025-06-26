package com.fomaxtro.notemark.data.database.util

import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import com.fomaxtro.notemark.domain.util.Result

suspend fun <T> safeDatabaseCall(
    call: suspend () -> T
): Result<T, DatabaseError> {
    return try {
        Result.Success(call())
    } catch (e: SQLiteDiskIOException) {
        e.printStackTrace()

        Result.Error(DatabaseError.DISK_FULL)
    } catch (e: SQLiteFullException) {
        e.printStackTrace()

        Result.Error(DatabaseError.DISK_FULL)
    }
}