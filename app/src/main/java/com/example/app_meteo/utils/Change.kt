package com.example.app_meteo.utils

import com.example.app_meteo.R
import java.util.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object  Change {
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

    fun capitalizeFirstLetter(word: String): String {
        return word.capitalize()
    }
    fun getDayOfWeekFromString(dateString: String): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val date = sdf.parse("$dateString/2024")

        val dayFormat = SimpleDateFormat("EEE", Locale.FRANCE)

        return capitalizeFirstLetter(dayFormat.format(date!!))
    }


    fun timestampToReadableDate(timestamp: Long): String {
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Europe/Paris"))
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return localDateTime.format(formatter)
        }

        fun formatDates(date: String): String {
            return "${date.substring(8, 10)}/${date.substring(5, 7)}"

        }

    fun weatherCodetoIcon(weatherCode: Int): Int {
        return when (weatherCode) {
            in 1..49 -> R.drawable.cloudy_sunny
            in 51..99 -> R.drawable.rain
            else -> R.drawable.sun
        }
    }
    }
