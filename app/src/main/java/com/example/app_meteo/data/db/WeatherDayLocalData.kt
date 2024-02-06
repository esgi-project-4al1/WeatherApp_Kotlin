package com.example.app_meteo.data.db

import android.content.Context
import com.example.app_meteo.model.modelSimpleDays.DataWeather
import com.google.gson.Gson

class WeatherDayLocalData (context: Context) {

        private val sharedPref = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        private val gson = Gson()

        fun getData() : DataWeather? {
            val data = sharedPref.getString("WeatherDayLocalData", null)
            return gson.fromJson(data, DataWeather::class.java)
        }

        fun sendData(weatherData: DataWeather) {
            val editor = sharedPref.edit()
            val data = gson.toJson(weatherData)
            editor.putString("WeatherDayLocalData", data)
            editor.apply()
        }

    }
