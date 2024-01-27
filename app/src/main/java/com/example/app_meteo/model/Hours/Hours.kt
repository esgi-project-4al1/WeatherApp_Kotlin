package com.example.app_meteo.model.Hours

data class Hours(
  val temperature_2m: List<Double>,
  val time: List<String>,
  val weathercode: List<Int>
)
