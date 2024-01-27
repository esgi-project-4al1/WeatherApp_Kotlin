package com.example.app_meteo.data.remote

import com.example.app_meteo.model.Hours.HoursModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HoursServices {
        @GET("/v1/forecast")
        suspend fun getWeather(
            @Query("latitude") latitude: String,
            @Query("longitude") longitude: String,
            @Query("hourly") hourly: String,
            @Query("hourly") weatherCode: String,
            @Query("forecast_days") forecastDays: Int,
            @Query("timezone") timezone: String): Response<HoursModel>

}