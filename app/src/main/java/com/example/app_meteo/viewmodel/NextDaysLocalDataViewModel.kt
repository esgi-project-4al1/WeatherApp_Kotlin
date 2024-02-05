package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.local.NextDaysLocalData
import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
import com.example.app_meteo.model.nextDaysModel.NextDays
import com.example.app_meteo.model.nextDaysModel.NextDaysModel

class NextDaysLocalDataViewModel( private val repository: NextDaysLocalDataRepository) : ViewModel(){

    fun getData(): NextDays? {
        return repository.getData()
    }

    fun sendData(weatherData: NextDays) {
        repository.sendData(weatherData)
    }
}