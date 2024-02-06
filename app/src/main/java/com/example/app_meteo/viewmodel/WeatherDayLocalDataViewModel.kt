package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.WeatherDayLocalDataRepository
import com.example.app_meteo.model.modelSimpleDays.DataWeather

class WeatherDayLocalDataViewModel ( private val weatherDayLocalDataRepository : WeatherDayLocalDataRepository) : ViewModel() {
    fun getData(): DataWeather? {
        return weatherDayLocalDataRepository.getData()
    }

    fun sendData(weatherData: DataWeather) {
        weatherDayLocalDataRepository.sendData(weatherData)
    }


}