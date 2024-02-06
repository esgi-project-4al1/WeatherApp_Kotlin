package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.modelSimpleDays.DataWeather

class WeatherViewModel( private val weatherRepository: WeatherRepository) : ViewModel(){

    suspend fun  getTheWeather(lat: Double , lon : Double , appID : String){
            weatherRepository.getWeather(lat,lon,appID)

    }

    val weatherLiveData: LiveData<DataWeather>
        get() = weatherRepository.weather


}