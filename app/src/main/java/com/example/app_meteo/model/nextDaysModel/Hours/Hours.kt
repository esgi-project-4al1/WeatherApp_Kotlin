package com.example.app_meteo.model.nextDaysModel.Hours

data class Hours(
  val temperature_2m: List<Double>,
  val time: List<String>,
  val weathercode: List<Int>
)
