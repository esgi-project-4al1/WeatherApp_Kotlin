package com.example.app_meteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_meteo.vue.NextDaysFragment
import com.example.app_meteo.vue.WeatherDayFragement
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_meteo.data.RetrofitObject
import com.example.app_meteo.data.db.NextDaysLocalData
import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
import com.example.app_meteo.data.repository.NextdaysRepository
import com.example.app_meteo.data.services.NextdaysService
import com.example.app_meteo.model.modelSimpleDays.DayItem
import com.example.app_meteo.model.nextDaysModel.Daily
import com.example.app_meteo.utils.Change
import com.example.app_meteo.utils.Constants
import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModel
import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModelFactory
import com.example.app_meteo.viewmodel.NextDaysViewModel
import com.example.app_meteo.viewmodel.NextDaysViewModelFactory

import com.example.app_meteo.vue.DaysAdapter
import com.example.app_meteo.vue.InternetConnection
import com.example.app_meteo.vue.LocationTask
import com.example.app_meteo.vue.PassData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.TimeZone
import kotlin.math.log

class MainActivity : AppCompatActivity()  {
    private lateinit var recyclerView: RecyclerView
    private lateinit var nextDaysViewModel: NextDaysViewModel
    private lateinit var nextDaysLocalDataViewModel: NextDaysLocalDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val recyclerView :RecyclerView = findViewById(R.id.recyclerView)

        // Initialize view models
        initNextDaysViewModel()
        initNextDaysLocalData()

        // Get weather data
        getNextDaysWeatherByCurrentLocation()

        val nextDaysData = getNextDaysLocalData()
        var nextDaysUi = nextDaysData?.let { sendDataToUI(it) }
        nextDaysViewModel.weatherLiveData.observe(this) {
            if(it != null) {
                Log.d("NextDaysFragment", "Observer triggered with data: $it")
                if(it.daily != null) {
                   nextDaysUi =   sendDataToUI(it.daily!!)
                    sendDatatoDb(it.daily!!)
                }
                else {
                    Log.d("id.daily", "Null")
                }
            }
        }



            val adapter = nextDaysUi?.let { DaysAdapter(it) }
            Log.d("Adapter" , "data : $nextDaysUi")
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

    }

    private fun getNextDaysLocalData(): Daily? {

        return nextDaysLocalDataViewModel.getData()
    }

    private fun initNextDaysLocalData() {
        val service = NextDaysLocalData(this)
        val repository = NextDaysLocalDataRepository(service)

        nextDaysLocalDataViewModel = ViewModelProvider(
            this,
            NextDaysLocalDataViewModelFactory(repository)
        )[NextDaysLocalDataViewModel::class.java]
    }


    private fun initNextDaysViewModel() {
        val service = RetrofitObject.getInstance(Constants.OpenMeteo_API_BASE_URL).create(
            NextdaysService::class.java
        )
        val repository = NextdaysRepository(service)

        nextDaysViewModel = ViewModelProvider(this, NextDaysViewModelFactory(repository))[NextDaysViewModel::class.java]

    }

    fun getNextDaysWeatherByCurrentLocation() {
        val locationTask = LocationTask(object : LocationTask.LocationCallback {
            override fun onLocationFetched(latitude: Double, longitude: Double) {
                Log.e("Latitude", "LAtitude: ${latitude}")
                callAPIServiceNextDays(latitude, longitude)
            }

            override fun onLocationFetchError(errorMessage: String) {
            }
        })
        locationTask.execute()
    }


    private fun callAPIServiceNextDays(lat: Double, lon: Double) {
        val daily = listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset")

        if(InternetConnection.isNetworkAvailable(this)) {

            lifecycleScope.launch {
                nextDaysViewModel.getTheWeather(lat, lon, daily, TimeZone.getDefault().id, Constants.NEXT7DAYS_WEATHER_DAYS)
            }
        } else {
            Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: No internet connection, showing snackbar")

            val snackBar =
                Snackbar.make(findViewById(android.R.id.content), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(R.string.app_name) {
                if (InternetConnection.isNetworkAvailable(this)) {

                    lifecycleScope.launch {
                        nextDaysViewModel.getTheWeather(lat, lon, daily, TimeZone.getDefault().id, 7)
                    }
                } else {
                    Toast.makeText(
                        this,
                        "No internet connection. Please try later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.show()
        }
    }

    private fun sendDatatoDb(nextDaysData: Daily) {
        nextDaysLocalDataViewModel.sendData(nextDaysData)
    }




    private fun sendDataToUI(nextDaysData: Daily): List<DayItem> {
        // Day 1
        // icons 0 or 100 == sunny
        //  1 to 50 == cloud sunny
        // 51  to 99 = cloudy

        val temperaturMaxDay0 = (nextDaysData.temperature_2m_max[0]).toString()
        val temperatureMinDay0 = (nextDaysData.temperature_2m_min[0]).toString()
        val dateDay0 = Change.formatDates(nextDaysData.sunrise[0])
        val iconetypeDay0 = Change.weatherCodetoIcon(nextDaysData.weathercode[0])

        val temperaturMaxDay1 = (nextDaysData.temperature_2m_max[1]).toString()
        val temperatureMinDay1 = (nextDaysData.temperature_2m_min[1]).toString()
        val dateDay1 = Change.formatDates(nextDaysData.sunrise[1]).toString()
        val iconetypeDay1 = Change.weatherCodetoIcon(nextDaysData.weathercode[1])


        // Day2
        val temperaturMaxDay2 = (nextDaysData.temperature_2m_max[2]).toString()
        val temperatureMinDay2 = (nextDaysData.temperature_2m_min[2]).toString()
        val dateDay2 = Change.formatDates(nextDaysData.sunrise[2])
        val iconetypeDay2 = Change.weatherCodetoIcon(nextDaysData.weathercode[2])


        //Day3
        val temperaturMaxDay3 = (nextDaysData.temperature_2m_max[3]).toString()
        val temperatureMinDay3 = (nextDaysData.temperature_2m_min[3]).toString()
        val dateDay3 = Change.formatDates(nextDaysData.sunrise[3])
        val iconetypeDay3 = Change.weatherCodetoIcon(nextDaysData.weathercode[3])


        //Day4
        val temperaturMaxDay4 = (nextDaysData.temperature_2m_max[4]).toString()
        val temperatureMinDay4 = (nextDaysData.temperature_2m_min[4]).toString()
        val dateDay4 = Change.formatDates(nextDaysData.sunrise[4])
        val iconetypeDay4 = Change.weatherCodetoIcon(nextDaysData.weathercode[4])


        //Day5
        val temperaturMaxDay5 = (nextDaysData.temperature_2m_max[5]).toString()
        val temperatureMinDay5 = (nextDaysData.temperature_2m_min[5]).toString()
        val dateDay5 = Change.formatDates(nextDaysData.sunrise[5])
        val iconetypeDay5 = Change.weatherCodetoIcon(nextDaysData.weathercode[5])


        //Day6
        val temperaturMaxDay6 = (nextDaysData.temperature_2m_max[6]).toString()
        val temperatureMinDay6 = (nextDaysData.temperature_2m_min[6]).toString()
        val dateDay6 = Change.formatDates(nextDaysData.sunrise[6])
        val iconetypeDay6 = Change.weatherCodetoIcon(nextDaysData.weathercode[6])


        //Day7



        Log.d("ListNextDays","dayitem :$dateDay0 , $temperaturMaxDay0 , $temperatureMinDay0 ")

        return listOf(
            DayItem(dateDay0, iconetypeDay0, temperatureMinDay0, temperaturMaxDay0),
            DayItem(dateDay1, iconetypeDay1, temperatureMinDay1, temperaturMaxDay1),
            DayItem(dateDay2, iconetypeDay2, temperatureMinDay2, temperaturMaxDay2),
            DayItem(dateDay3, iconetypeDay3, temperatureMinDay3, temperaturMaxDay3),
            DayItem(dateDay4, iconetypeDay4, temperatureMinDay4, temperaturMaxDay4),
            DayItem(dateDay5, iconetypeDay5, temperatureMinDay5, temperaturMaxDay5),
            DayItem(dateDay6, iconetypeDay6, temperatureMinDay6, temperaturMaxDay6)

        )
    }



}

