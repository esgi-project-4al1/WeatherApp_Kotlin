package com.example.app_meteo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.remote.NextdaysService
import com.example.app_meteo.model.nextDaysModel.NextDaysModel
class NextdaysRepository ( private  val  service: NextdaysService) {
    private val nextSevenDaysWeatherLiveData = MutableLiveData<NextDaysModel>()

    val weatherLiveData: LiveData<NextDaysModel>
        get() = nextSevenDaysWeatherLiveData

    suspend fun getTheWeather(latitude: String, longitude: String, dailyParameters: List<String>, timezone: String, forecastDays: Int) {
        val result = service.getTheWeather(latitude, longitude, dailyParameters, timezone, forecastDays)

        if(result.body() != null) {
            nextSevenDaysWeatherLiveData.value = result.body()
        }
    }
}