package com.example.app_meteo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.services.SearchService
import com.example.app_meteo.model.modelSimpleDays.CityData

class SearchRepository (val locationApiService: SearchService)
{
    suspend fun getLocation(cityName: String, appId: String): Pair<Double, Double>? {
        val response = locationApiService.getLocation(cityName, appId)
        if (response.isSuccessful) {
            val cityDataList = response.body()
            if (!cityDataList.isNullOrEmpty()) {
                val cityData = cityDataList[0]
                return Pair(cityData.lat ?: 0.0, cityData.lon ?: 0.0)
            }
        }
        return null
    }


    }

