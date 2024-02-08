package com.example.app_meteo.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.model.modelSimpleDays.CityData
import com.example.app_meteo.data.services.LocationApiService
class CityRepository(private val locationApiService: LocationApiService) {

    private val cityLiveData = MutableLiveData<List<CityData>>()

    val citySearchResults: LiveData<List<CityData>>
        get() = cityLiveData

    suspend fun searchCity(query: String, apiKey: String) {
        val result = locationApiService.getCityCoordinates(query, apiKey)

        if (result.isSuccessful) {
            val cities = result.body()
            cityLiveData.value = cities
        }
    }
}
