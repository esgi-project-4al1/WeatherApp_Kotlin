package com.example.app_meteo.data.repository

import com.example.app_meteo.data.db.WeatherDayLocalData
import com.example.app_meteo.model.modelSimpleDays.DataWeather

class WeatherDayLocalDataRepository (private val weatherDayLocalDataService: WeatherDayLocalData ) {

        fun getData(): DataWeather? {
            return weatherDayLocalDataService.getData()
        }

        fun sendData(weatherData: DataWeather) {
            weatherDayLocalDataService.sendData(weatherData)
        }

    }