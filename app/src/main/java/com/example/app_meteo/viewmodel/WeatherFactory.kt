package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_meteo.data.repository.WeatherRepository

class WeatherFactory (private  val weatherRepository: WeatherRepository) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
           return WeatherViewModel(weatherRepository )as T
    }

}