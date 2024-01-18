package com.example.app_meteo.model.nextDaysModel

data class NextDays(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_max: List<Double>,
    val temperature_min: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>)
