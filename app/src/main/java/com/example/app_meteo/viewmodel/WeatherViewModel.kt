package com.example.app_meteo.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.WeatherModel

class WeatherViewModel( private val weatherRepository: WeatherRepository) : ViewModel(){

    private val _weatherLiveData = MutableLiveData<WeatherModel>()
    val weatherLiveData: LiveData<WeatherModel>
        get() = _weatherLiveData
    suspend fun  getTheWeather(lat: String , lon : String , appID : String){
            weatherRepository.getWeather(lat,lon,appID)
        _weatherLiveData.value = weatherRepository.weather.value

    }


}