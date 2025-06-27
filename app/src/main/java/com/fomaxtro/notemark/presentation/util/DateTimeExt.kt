package com.fomaxtro.notemark.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toScopedDate(): String {
    val now = Instant
        .now()
        .atZone(ZoneId.systemDefault())

    val formatter = if (year < now.year) {
        DateTimeFormatter.ofPattern("d MMM yyyy")
    } else {
        DateTimeFormatter.ofPattern("dd MMM")
    }

    return format(formatter).uppercase()
}