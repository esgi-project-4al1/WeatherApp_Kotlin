package com.example.app_meteo.data.services

import com.example.app_meteo.model.modelSimpleDays.CityData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService
{

            @GET("/geo/1.0/direct")
        suspend fun getLocation(@Query("q") q: String, @Query("appid") appId: String) : Response<List<CityData>>


}