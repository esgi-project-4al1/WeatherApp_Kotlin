package com.example.app_meteo.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.model.DataWeather
import com.example.app_meteo.utils.Constants.OpenWeatherMap_api_key
import com.example.app_meteo.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@SuppressLint("StaticFieldLeak")
class WeatherDaysViewModel() : ViewModel() {

    private lateinit var countryTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var tempMinTextView: TextView
    private lateinit var tempMaxTextView: TextView
    private lateinit var sunriseTextView: TextView
    private lateinit var sunsetTextView: TextView
    private lateinit var windTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var pressureTextView: TextView

    // Initialisation des références aux vues
    fun initViews(
        countryTextView: TextView,
        dateTextView: TextView,
        tempTextView: TextView,
        infoTextView: TextView,
        tempMinTextView: TextView,
        tempMaxTextView: TextView,
        sunriseTextView: TextView,
        sunsetTextView: TextView,
        windTextView: TextView,
        humidityTextView: TextView,
        pressureTextView: TextView
    ) {
        Log.e("WeatherViewModel", "je vais lancer les fi")
        this.countryTextView = countryTextView
        this.dateTextView = dateTextView
        this.tempTextView = tempTextView
        this.infoTextView = infoTextView
        this.tempMinTextView = tempMinTextView
        this.tempMaxTextView = tempMaxTextView
        this.sunriseTextView = sunriseTextView
        this.sunsetTextView = sunsetTextView
        this.windTextView = windTextView
        this.humidityTextView = humidityTextView
        this.pressureTextView = pressureTextView
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getWeatherByCurrentLocation() {
        val locationTask = LocationTask(object : LocationTask.LocationCallback {
            override fun onLocationFetched(latitude: Double, longitude: Double) {
                Log.e("Latitude", "LAtitude: ${latitude}")
                callWeatherApi(latitude, longitude)
            }

            override fun onLocationFetchError(errorMessage: String) {
            }
        })
        locationTask.execute()
    }

    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    private fun callWeatherApi(latitude: Double, longitude: Double) {
        val call = weatherApiService.getWeatherByCoordinates(latitude, longitude, OpenWeatherMap_api_key)

         call.enqueue(object : Callback<DataWeather> {
                override fun onResponse(call: Call<DataWeather>, response: Response<DataWeather>) {
                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        weatherResponse?.let { weatherData ->
                            val temperatureCelsius = (weatherData.main?.temp?.let { Utils.kelvinToCelsius(it) })?.toInt()
                            val temperatureCelsiusMax = (weatherData.main?.tempMax.let { Utils.kelvinToCelsius(it!!) })?.toInt()
                            val temperatureCelsiusMin = (weatherData.main?.tempMin?.let { Utils.kelvinToCelsius(it) })?.toInt()
                            val windSpeedKmPerHour = (weatherData.wind?.speed?.let { Utils.metersPerSecondToKmPerHour(it) })?.toInt()
                            val sunriseTime = weatherData.sys?.sunrise?.let { Utils.unixTimestampToLocalTime(it.toLong()) }
                            val sunsetTime = weatherData.sys?.sunset?.let { Utils.unixTimestampToLocalTime(it.toLong()) }
                            val readableDate = weatherData.dt?.let { Utils.timestampToReadableDate(it.toLong()) }

                            countryTextView.text = "${weatherData.name}"
                            dateTextView.text = "$readableDate"
                            tempTextView.text = "$temperatureCelsius°"
                            infoTextView.text = "${weatherData.weather[0].description}"
                            tempMinTextView.text = "$temperatureCelsiusMin°"
                            tempMaxTextView.text = "$temperatureCelsiusMax°"
                            sunriseTextView.text = "$sunriseTime"
                            sunsetTextView.text = "$sunsetTime"
                            windTextView.text = "$windSpeedKmPerHour km/h"
                            humidityTextView.text = "${weatherData.main?.humidity}%"
                            pressureTextView.text = "${weatherData.main?.pressure}"
                        }
                    }
                }

            override fun onFailure(call: Call<DataWeather>, t: Throwable) {
                // Gérer les erreurs de requête
                Log.e("WeatherViewModel", "Erreur de requête: ${t.message}")
            }
        })
    }

}