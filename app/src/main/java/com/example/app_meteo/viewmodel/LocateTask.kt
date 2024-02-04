package com.example.app_meteo.viewmodel

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class LocationTask(private val callback: LocationCallback) {

    fun execute() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = URL("https://ipinfo.io/loc").readText(Charsets.UTF_8)
                Log.e("Response", "${response} ")
                handleResponse(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "erreur ")
            }
        }
    }

    private fun handleResponse(response: String) {
        val coordinates = response.split(",")
        if (coordinates.size == 2) {
            val latitude = coordinates[0].toDouble()
            val longitude = coordinates[1].toDouble()
            callback.onLocationFetched(latitude, longitude)
        } else {
            Log.e("WeatherViewModel", "erreurhandleresponse")
        }
    }

    interface LocationCallback {
        fun onLocationFetched(latitude: Double, longitude: Double)
        fun onLocationFetchError(errorMessage: String)
    }
}
