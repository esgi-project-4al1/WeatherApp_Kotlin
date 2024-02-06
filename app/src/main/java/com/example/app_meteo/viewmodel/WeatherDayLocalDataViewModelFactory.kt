package com.example.app_meteo.viewmodel

import com.example.app_meteo.data.repository.WeatherDayLocalDataRepository;
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class WeatherDayLocalDataViewModelFactory (private val weatherDayLocalDataRepository: WeatherDayLocalDataRepository ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherDayLocalDataViewModel(weatherDayLocalDataRepository) as T
        }

    }

