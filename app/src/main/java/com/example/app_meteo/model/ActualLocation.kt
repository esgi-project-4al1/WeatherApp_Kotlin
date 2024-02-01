package com.example.app_meteo.model

data class ActualLocation(
    val ip: String,
    val loc: String,
    val city: String,
    val country: String,
    val postal: String,
    val region: String,
    val timezone: String
)
