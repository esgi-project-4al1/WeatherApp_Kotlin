package com.example.app_meteo.model

data class WeatherModel(
    val base: String,
    val clouds: Cloud,
    val cod: Int,
    val coord: Coordinations,
    val dt: Int,
    val id: Int,
    val main: Data,
    val name: String,
    val sys: Sun,
    val timezone: Int,
    val visibility: Int,
    val weather: List<OneWeather>,
    val wind: Wind
)
