package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.database.util.DatabaseError
import com.fomaxtro.notemark.domain.error.DataError

fun DatabaseError.toDataError(): DataError {
    return when (this) {
        DatabaseError.DISK_FULL -> DataError.DISK_FULL
    }
}