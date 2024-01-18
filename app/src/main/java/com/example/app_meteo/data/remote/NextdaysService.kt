package com.example.app_meteo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.app_meteo.model.nextDaysModel.NextDaysModel

interface NextdaysService {
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") dailyParameters: List<String>,
        @Query("timezone") timezone: String,
        @Query("forecast_days") forecastDays: Int
    ): Response<NextDaysModel>

}