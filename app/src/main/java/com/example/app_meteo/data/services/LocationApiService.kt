package com.example.app_meteo.data.services

import com.example.app_meteo.model.modelSimpleDays.CityData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("/geo/1.0/direct")
    suspend fun getCityCoordinates(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Response<List<CityData>>
}
