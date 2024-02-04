package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.WeatherModel

class WeatherViewModel( private val weatherRepository: WeatherRepository) {

    suspend fun  getTheWeather(lat: String , lon : String , appID : String){
            weatherRepository.getWeather(lat,lon,appID)
    }

   val weather: LiveData<WeatherModel>
       get() = weatherRepository.weather

}