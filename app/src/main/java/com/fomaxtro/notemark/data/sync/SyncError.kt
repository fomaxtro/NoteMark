package com.fomaxtro.notemark.data.sync

import com.fomaxtro.notemark.domain.util.Error

enum class SyncError : Error {
    UNAUTHORIZED,
    FAILED_TO_FETCH_NOTES,
    FAILED_TO_CREATE_NOTE,
    FAILED_TO_UPDATE_NOTE,
    FAILED_TO_DELETE_NOTE
}