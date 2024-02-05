package com.example.app_meteo.data.local

import android.content.Context
import android.util.Log
import com.example.app_meteo.model.nextDaysModel.NextDays
import com.example.app_meteo.model.nextDaysModel.NextDaysModel
import com.google.gson.Gson

class NextDaysLocalData(context: Context) {

        private val sharedPref = context.getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        private val gson = Gson()

        fun sendData(weatherData: NextDays) {
            val data = gson.toJson(weatherData)
            Log.d("NextDaysLocalData", "Saving data: $data")

            val editor = sharedPref.edit()
            editor.putString("NextDaysData", data)
            editor.apply()
        }

        fun getData(): NextDays? {
            val data = sharedPref.getString("NextDaysData", null)
            Log.d("NextDaysFragment", "in getdata service, retrieved data: $data")

            return gson.fromJson(data, NextDays::class.java)


        }

    }
