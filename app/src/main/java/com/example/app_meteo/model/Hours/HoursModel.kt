package com.example.app_meteo.model.Hours

data class HoursModel(
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hours,
    val hourly_units: HoursUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int


)
