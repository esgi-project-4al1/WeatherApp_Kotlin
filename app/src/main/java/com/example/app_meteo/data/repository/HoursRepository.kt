package com.example.app_meteo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.remote.HoursServices
import com.example.app_meteo.model.Hours.HoursModel

class HoursRepository (private  val  service: HoursServices) {
        private val HoursLiveData = MutableLiveData<HoursModel>()

        val weatherLiveData: LiveData<HoursModel>
            get() = HoursLiveData

      suspend fun getTheWeather(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String) {
        val result = service.getWeather(lat, lon, hourly, weatherCode, forecastDays, timezone)


        if(result.body() != null) {
                HoursLiveData.value = result.body()
            }
        }
    }
