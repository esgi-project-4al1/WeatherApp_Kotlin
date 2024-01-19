package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.example.app_meteo.data.repository.NextdaysRepository
import com.example.app_meteo.model.nextDaysModel.NextDaysModel

class NextDaysViewModel (private val nextdaysrepository: NextdaysRepository) : ViewModel() {

        suspend fun getTheWeather(latitude: String, longitude: String, dailyParameters: List<String>, timezone: String, forecastDays: Int) {
            nextdaysrepository.getTheWeather(latitude, longitude, dailyParameters, timezone, forecastDays)
        }

        val weatherLiveData: LiveData<NextDaysModel>
            get() = nextdaysrepository.weatherLiveData

    }
