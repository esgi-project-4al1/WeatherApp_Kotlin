package com.example.app_meteo.vue


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.net.UnknownHostException

class LocationTask(private val callback: LocationCallback) {

    fun execute() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = URL("https://ipinfo.io/loc").readText(Charsets.UTF_8)
                Log.e("LocationTask", "${response} ")
                handleResponse(response)
            } catch (e: UnknownHostException) {
                Log.e("LocationTask", "Erreur de résolution d'hôte: ${e.message}")
                callback.onLocationFetchError("Erreur de résolution d'hôte")
            } catch (e: Exception) {
                Log.e("LocationTask", "Erreur inattendue: ${e.message}")
                callback.onLocationFetchError("Erreur inattendue")
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
            Log.e("LocationTask", "Erreur dans la manipulation de la réponse")
            callback.onLocationFetchError("Erreur dans la manipulation de la réponse")
        }
    }

    interface LocationCallback {
        fun onLocationFetched(latitude: Double, longitude: Double)
        fun onLocationFetchError(errorMessage: String)
    }
}