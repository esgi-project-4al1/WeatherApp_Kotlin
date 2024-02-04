package com.example.app_meteo.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Utils {
    fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    fun metersPerSecondToKmPerHour(metersPerSecond: Double): Double {
        return metersPerSecond * 3.6
    }

    fun unixTimestampToLocalTime(timestamp: Long): String {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Europe/Paris"))
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return localDateTime.format(formatter)
    }

    fun timestampToReadableDate(timestamp: Long): String {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Europe/Paris"))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return localDateTime.format(formatter)
    }
}
