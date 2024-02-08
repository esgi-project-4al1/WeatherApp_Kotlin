package com.example.app_meteo.model.modelSimpleDays

import com.google.gson.annotations.SerializedName

data class CityData(


    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,

)
