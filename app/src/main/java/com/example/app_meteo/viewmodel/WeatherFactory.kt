package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.WeatherRepository

class WeatherFactory (private  val weatherRepository: WeatherRepository){

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModel> create(modelClass: Class<T>): T {
           return WeatherViewModel(weatherRepository )as T
    }

}