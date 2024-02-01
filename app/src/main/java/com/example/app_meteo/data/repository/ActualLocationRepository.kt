package com.example.app_meteo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_meteo.data.remote.ActualLocationService
import com.example.app_meteo.model.ActualLocation

class ActualLocationRepository ( private  val actualLocation: ActualLocationService){

      private  val actualLocationLiveData = MutableLiveData<ActualLocation>()

  val  getlocation: LiveData<ActualLocation>
      get() = actualLocationLiveData

  suspend fun  getActualLocation(){
      val result = actualLocation.getActualLocation()
      if (result.body() != null)
      {
               actualLocationLiveData.postValue(result.body())
      }
  }

}