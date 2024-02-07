package com.example.app_meteo.vue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_meteo.data.db.WeatherDayLocalData
import com.example.app_meteo.data.repository.WeatherDayLocalDataRepository
import com.example.app_meteo.model.modelSimpleDays.DataWeather
import com.example.app_meteo.viewmodel.WeatherDayLocalDataViewModel
import com.example.app_meteo.viewmodel.WeatherDayLocalDataViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.app_meteo.data.RetrofitObject
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.utils.Constants.OpenWeatherMap_API_BASE_URL
import com.example.app_meteo.utils.Constants.OpenWeatherMap_api_key
import com.example.app_meteo.viewmodel.WeatherFactory
import com.example.app_meteo.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar

import com.example.app_meteo.R
import com.example.app_meteo.utils.Change

class WeatherDayFragement() : Fragment(){

     private  lateinit var  weatherDayLocalDataViewModel : WeatherDayLocalDataViewModel
     private lateinit var  weatherViewModel : WeatherViewModel




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initWeatherDayLocalData()
        initNextDaysViewModel()
        Log.d("WeatherTest", "Launching Api ")

        getWeatherByCurrentLocation()
        Log.d("WeatherTest", "After calling API")
        val weatherData = getWeatherDayLocalData()

        weatherViewModel.weatherLiveData.observe(requireActivity()) {
            if (it != null) {
                Log.d("NextDaysFragment", "Observer triggered with data: $it")
                if (it != null) {
                    //setNext7DaysWeatherToUI(it.daily!!)
                    sendDatatoDb(it)
                    (it!!)
                } else {
                    Log.d("id.daily", "Null")
                }
                val dataList = sendDatatoUi(weatherData!!)

                Log.d("WeatherTest", "WeatherData: $weatherData")
            }
        }

    }


    private fun getWeatherDayLocalData(): DataWeather? {

        return weatherDayLocalDataViewModel.getData()
    }

    private fun initWeatherDayLocalData() {
        val service = WeatherDayLocalData(requireActivity())
        val repository = WeatherDayLocalDataRepository(service)

        weatherDayLocalDataViewModel = ViewModelProvider(
            requireActivity(),
            WeatherDayLocalDataViewModelFactory(repository)
        )[WeatherDayLocalDataViewModel::class.java]
    }


    private fun initNextDaysViewModel() {
        val service = RetrofitObject.getInstance(OpenWeatherMap_API_BASE_URL).create(
            WeatherApiService::class.java
        )
        val repository = WeatherRepository(service)

        weatherViewModel = ViewModelProvider(requireActivity(), WeatherFactory(repository))[WeatherViewModel::class.java]

    }


    private fun callAPIServiceWeatherDay(lat: Double, lon: Double) {


        if(InternetConnection.isNetworkAvailable(requireActivity())) {

            lifecycleScope.launch {
                weatherViewModel.getTheWeather(lat, lon, OpenWeatherMap_api_key)
            }
        } else {
            Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: No internet connection, showing snackbar")

            val snackBar =
                Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(R.string.app_name) {
                if (InternetConnection.isNetworkAvailable(requireActivity())) {

                    lifecycleScope.launch {
                        weatherViewModel.getTheWeather(lat, lon, OpenWeatherMap_api_key)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "No internet connection. Please try later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.show()
        }
    }

    private fun   sendDatatoDb(dataWeather: DataWeather) {
        weatherDayLocalDataViewModel.sendData(dataWeather)
    }

    fun getWeatherByCurrentLocation() {
        val locationTask = LocationTask(object : LocationTask.LocationCallback {
            override fun onLocationFetched(latitude: Double, longitude: Double) {
                Log.e("Latitude", "LAtitude: ${latitude}")
                callAPIServiceWeatherDay(latitude, longitude)
            }

            override fun onLocationFetchError(errorMessage: String) {
            }
        })
        locationTask.execute()
    }

    fun sendDatatoUi( weatherData: DataWeather) : List<Any?>
    {
        //Temperature
        val temperatureCelsius = (weatherData.main?.temp?.let { Change.kelvinToCelsius(it) })?.toInt()
        //Te
        val temperatureCelsiusMax = (weatherData.main?.tempMax.let { Change.kelvinToCelsius(it!!) })?.toInt()
        val temperatureCelsiusMin = (weatherData.main?.tempMin?.let { Change.kelvinToCelsius(it) })?.toInt()
        val windSpeedKmPerHour = (weatherData.wind?.speed?.let { Change.metersPerSecondToKmPerHour(it) })?.toInt()
        val sunriseTime = weatherData.sys?.sunrise?.let { Change.unixTimestampToLocalTime(it.toLong()) }
        val sunsetTime = weatherData.sys?.sunset?.let { Change.unixTimestampToLocalTime(it.toLong()) }
        val readableDate = weatherData.dt?.let { Change.timestampToReadableDate(it.toLong()) }
        val humidity = weatherData.main?.humidity
        val pressure = weatherData.main?.pressure
        val country = weatherData.name
        val infotext = weatherData.weather[0].description

        val dataWeather = listOf(
            temperatureCelsius,
            temperatureCelsiusMax,
            temperatureCelsiusMin,
            windSpeedKmPerHour,
            sunriseTime,
            sunsetTime,
            readableDate,
            humidity,
            pressure,
            country,
            infotext
        )

        return dataWeather


    }



}