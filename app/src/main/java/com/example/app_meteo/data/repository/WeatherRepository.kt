package com.example.app_meteo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.model.modelSimpleDays.DataWeather

class WeatherRepository(private val weatherService: WeatherApiService) {

     private val weatherLiveData = MutableLiveData<DataWeather>()


    val weather : LiveData<DataWeather>
        get() = weatherLiveData

    suspend fun getWeather(lat: Double , lon : Double , appID : String){
         val result = weatherService.getWeatherByCoordinates(lat,lon,appID)
        Log.d("Repository", "in repository before check , result is : ${result.body()}")
       if(result.body() != null )
       {
            weatherLiveData.value = result.body()
       }
   }
}