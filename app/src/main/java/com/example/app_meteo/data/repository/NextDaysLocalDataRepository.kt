package com.example.app_meteo.data.repository

import com.example.app_meteo.data.db.NextDaysLocalData
import com.example.app_meteo.model.nextDaysModel.Daily

class NextDaysLocalDataRepository( private val  service : NextDaysLocalData) {
    fun sendData(weatherData: Daily) {
        service.sendData(weatherData)
    }

    fun getData(): Daily? {
        return service.getData()
    }
}