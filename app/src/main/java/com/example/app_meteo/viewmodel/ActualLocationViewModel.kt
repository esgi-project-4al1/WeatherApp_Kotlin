package com.example.app_meteo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.app_meteo.data.repository.ActualLocationRepository
import com.example.app_meteo.model.ActualLocation

class ActualLocationViewModel ( private val actuallocation : ActualLocationRepository) : ViewModel() {
   suspend fun getActualLocation(){
      actuallocation.getActualLocation()
   }

    val   getlocation : LiveData<ActualLocation>
        get() = actuallocation.getlocation
}