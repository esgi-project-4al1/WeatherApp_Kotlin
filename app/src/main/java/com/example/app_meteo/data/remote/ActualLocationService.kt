package com.example.app_meteo.data.remote

import com.example.app_meteo.model.ActualLocation
import retrofit2.Response
import retrofit2.http.GET

interface ActualLocationService      {
    @GET("/json")
    suspend fun getActualLocation(): Response<ActualLocation>
}