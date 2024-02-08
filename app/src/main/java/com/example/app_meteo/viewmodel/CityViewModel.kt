package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.CityRepository
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.modelSimpleDays.CityData
import com.example.app_meteo.model.modelSimpleDays.DataWeather
import com.example.app_meteo.utils.Constants.OpenWeatherMap_api_key

class CityViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    // LiveData pour les résultats de recherche de ville
    val citySearchResults: LiveData<List<CityData>> = cityRepository.citySearchResults

    // LiveData pour les données météorologiques
    val weatherData: LiveData<DataWeather> = weatherRepository.weather

    // Fonction de recherche de ville
    suspend fun searchCity(query: String) {
        cityRepository.searchCity(query, OpenWeatherMap_api_key)
    }

    // Fonction pour charger les données météorologiques en fonction des coordonnées
    suspend fun loadWeatherData(latitude: Double, longitude: Double) {
        weatherRepository.getWeather(latitude, longitude, OpenWeatherMap_api_key)
    }
}

