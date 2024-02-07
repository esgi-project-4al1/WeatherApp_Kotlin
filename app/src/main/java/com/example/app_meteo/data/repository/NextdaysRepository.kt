package com.example.app_meteo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.services.NextdaysService
import com.example.app_meteo.model.nextDaysModel.NextDaysModel
class NextdaysRepository ( private  val  service: NextdaysService) {
    private val nextSevenDaysWeatherLiveData = MutableLiveData<NextDaysModel>()

    val weatherLiveData: LiveData<NextDaysModel>
        get() = nextSevenDaysWeatherLiveData

    suspend fun getTheWeather(latitude: Double, longitude: Double, dailyParameters: List<String>, timezone: String, forecastDays: Int) {
        val result = service.getTheWeather(latitude, longitude, dailyParameters, timezone, forecastDays)
        Log.d("Repository", "in repository before check , result is : ${result.body()}")

        if(result.body() != null) {
            Log.d("NextDaysFragment", "in repository , result is : ${result.body()}")

            nextSevenDaysWeatherLiveData.value = result.body()
        }
    }
}