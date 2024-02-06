package com.example.app_meteo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.services.WeatherService
import com.example.app_meteo.model.WeatherModel

class WeatherRepository(private val weatherService: WeatherService) {

     private val weatherLiveData = MutableLiveData<WeatherModel>()


    val weather : LiveData<WeatherModel>
        get() = weatherLiveData

    suspend fun getWeather(lat: String , lon : String , appID : String){
         val result = weatherService.getTheWeather(lat,lon,appID)

        if(result.body() != null )
        {
               weatherLiveData.postValue(result.body())
        }
    }
}