package com.example.app_meteo.vue

import com.example.app_meteo.model.modelSimpleDays.DayItem

interface PassData {
    fun  PassWeatherDayData(data : List<Any?>)

    fun PassNextDaysData(data : List<DayItem>)


}