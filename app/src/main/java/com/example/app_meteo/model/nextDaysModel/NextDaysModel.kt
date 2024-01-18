package com.example.app_meteo.model.nextDaysModel


data class NextDaysModel(
    val daily : NextDays,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)
