package com.fomaxtro.notemark.presentation.util

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.ui.UiText
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun ZonedDateTime.toScopedDate(): String {
    val now = Instant
        .now()
        .atZone(ZoneOffset.UTC)

    val formatter = if (year < now.year) {
        DateTimeFormatter.ofPattern("d MMM yyyy")
    } else {
        DateTimeFormatter.ofPattern("dd MMM")
    }

    return format(formatter).uppercase()
}

fun Instant?.toSyncDateTimeUiText(): UiText {
    if (this == null) return UiText.StringResource(R.string.never_synced)

    val now = Instant.now()

    return when {
        this.until(now, ChronoUnit.MINUTES) < 5 -> {
            UiText.StringResource(R.string.just_now)
        }

        this.until(now, ChronoUnit.DAYS) <= 7 -> {
            when {
                this.until(now, ChronoUnit.DAYS) >= 1 -> {
                    val daysAgo = this.until(now, ChronoUnit.DAYS)

                    UiText.PluralStringResource(R.plurals.days_ago, daysAgo.toInt())
                }

                this.until(now, ChronoUnit.HOURS) >= 1 -> {
                    val hoursAgo = this.until(now, ChronoUnit.HOURS)

                    UiText.PluralStringResource(R.plurals.hours_ago, hoursAgo.toInt())
                }

                else -> {
                    val minutesAgo = this.until(now, ChronoUnit.MINUTES)

                    UiText.StringResource(R.string.minutes_ago, arrayOf(minutesAgo.toInt()))
                }
            }
        }

        else -> {
            val formattedDate = this
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))

            UiText.DynamicString(formattedDate)
        }
    }
}