package com.example.app_meteo.model.nextDaysModel

 import com.google.gson.annotations.SerializedName


data class Daily (

    @SerializedName("time"               ) var time             : ArrayList<String> = arrayListOf(),
    @SerializedName("weathercode"        ) var weathercode      : ArrayList<Int>    = arrayListOf(),
    @SerializedName("temperature_2m_max" ) var temperature_2m_max : ArrayList<Double> = arrayListOf(),
    @SerializedName("temperature_2m_min" ) var temperature_2m_min : ArrayList<Double>  = arrayListOf(),
    @SerializedName("sunrise"            ) var sunrise          : ArrayList<String> = arrayListOf(),
    @SerializedName("sunset"             ) var sunset           : ArrayList<String> = arrayListOf()

)
