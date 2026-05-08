package com.moravian.untitledcasino

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

expect fun currentTimeMillis(): Long

fun formatEpochMillis(epochMillis: Long): String {
    val dateTime = Instant
        .fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    return "${dateTime.date} $hour:$minute"
}
