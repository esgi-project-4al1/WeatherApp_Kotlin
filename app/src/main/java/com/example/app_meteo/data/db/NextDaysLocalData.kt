package com.example.app_meteo.data.db

import android.content.Context
import android.util.Log
import com.example.app_meteo.model.nextDaysModel.Daily
import com.google.gson.Gson

class NextDaysLocalData(context: Context) {

        private val sharedPref = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        private val gson = Gson()

        fun sendData(weatherData: Daily) {
            val data = gson.toJson(weatherData)
            Log.d("NextDaysLocalData", "Saving data: $data")

            val editor = sharedPref.edit()
            editor.putString("NextDaysData", data)
            editor.apply()
        }

        fun getData(): Daily? {
            val data = sharedPref.getString("NextDaysData", null)
            Log.d("NextDaysFragment", "in getdata service, retrieved data: $data")

            return gson.fromJson(data, Daily::class.java)


        }

    }
