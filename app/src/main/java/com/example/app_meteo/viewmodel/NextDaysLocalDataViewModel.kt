package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
import com.example.app_meteo.model.nextDaysModel.Daily

class NextDaysLocalDataViewModel( private val repository: NextDaysLocalDataRepository) : ViewModel(){

    fun getData(): Daily? {
        return repository.getData()
    }

    fun sendData(weatherData: Daily) {
        repository.sendData(weatherData)
    }
}