package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.HoursRepository
import com.example.app_meteo.model.Hours.HoursModel

class HoursViewModel( private val repository: HoursRepository) : ViewModel() {
    suspend fun getWeather(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String) {
        repository.getTheWeather(lat, lon ,hourly, weatherCode, forecastDays, timezone)
    }

    val weatherData : LiveData<HoursModel>
        get() = repository.weatherLiveData

}