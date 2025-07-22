package com.fomaxtro.notemark.domain.model

enum class SyncInterval(val interval: Int) {
    MANUAL_ONLY(0),
    FIFTEEN_MINUTES(15),
    THIRTY_MINUTES(30),
    ONE_HOUR(60);

    companion object {
        fun fromInterval(interval: Int): SyncInterval {
            return when (interval) {
                0 -> MANUAL_ONLY
                15 -> FIFTEEN_MINUTES
                30 -> THIRTY_MINUTES
                60 -> ONE_HOUR
                else -> throw IllegalArgumentException("Invalid interval: $interval")
            }
        }
    }
}