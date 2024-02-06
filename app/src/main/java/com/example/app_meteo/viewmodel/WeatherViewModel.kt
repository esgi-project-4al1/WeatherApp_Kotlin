package com.example.app_meteo.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.WeatherModel

class WeatherViewModel( private val weatherRepository: WeatherRepository) : ViewModel(){

    suspend fun  getTheWeather(lat: String , lon : String , appID : String){
            weatherRepository.getWeather(lat,lon,appID)

    }

    val weatherLiveData: LiveData<WeatherModel>
        get() = weatherRepository.weather


}