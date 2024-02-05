package com.example.app_meteo.data.repository

import com.example.app_meteo.data.local.NextDaysLocalData
import com.example.app_meteo.model.nextDaysModel.NextDays
import com.example.app_meteo.model.nextDaysModel.NextDaysModel

class NextDaysLocalDataRepository( private val  service : NextDaysLocalData) {
    fun sendData(weatherData: NextDays) {
        service.sendData(weatherData)
    }

    fun getData(): NextDays? {
        return service.getData()
    }
}