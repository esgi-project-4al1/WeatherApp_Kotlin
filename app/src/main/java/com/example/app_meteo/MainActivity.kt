package com.example.app_meteo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_meteo.data.RetrofitObject
import com.example.app_meteo.data.db.NextDaysLocalData
import com.example.app_meteo.data.db.WeatherDayLocalData
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
import com.example.app_meteo.data.repository.NextdaysRepository
import com.example.app_meteo.data.repository.SearchRepository
import com.example.app_meteo.data.repository.WeatherDayLocalDataRepository
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.data.services.NextdaysService
import com.example.app_meteo.data.services.SearchService
import com.example.app_meteo.model.modelSimpleDays.DataWeather
import com.example.app_meteo.model.modelSimpleDays.DayItem
import com.example.app_meteo.model.nextDaysModel.Daily
import com.example.app_meteo.utils.Change
import com.example.app_meteo.utils.Constants
import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModel
import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModelFactory
import com.example.app_meteo.viewmodel.NextDaysViewModel
import com.example.app_meteo.viewmodel.NextDaysViewModelFactory
import com.example.app_meteo.viewmodel.WeatherDayLocalDataViewModel
import com.example.app_meteo.viewmodel.WeatherDayLocalDataViewModelFactory
import com.example.app_meteo.viewmodel.WeatherFactory
import com.example.app_meteo.viewmodel.WeatherViewModel
import com.example.app_meteo.vue.DaysAdapter
import com.example.app_meteo.vue.InternetConnection
import com.example.app_meteo.vue.LocationTask
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.TimeZone
class MainActivity : AppCompatActivity()  {
    private lateinit var nextDaysViewModel: NextDaysViewModel
    private lateinit var nextDaysLocalDataViewModel: NextDaysLocalDataViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherDayLocalDataViewModel: WeatherDayLocalDataViewModel
    private var searchedLat: Double = 0.0
    private var searchedLon: Double = 0.0
    private lateinit var  weatherData: DataWeather
    private lateinit var nextdaysdata: Daily
    private lateinit var nextDaysUi : List<DayItem>
    private  var nextDaysUiSearched : List<DayItem>? = null


    fun OnDestroy(savedInstanceState: Bundle?) {


    }
    // Next days Implementation

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
        nextDaysViewModel = ViewModelProvider(
            this,
            NextDaysViewModelFactory(repository)
        )[NextDaysViewModel::class.java]
    }
    private fun getNextDaysWeatherByCurrentLocation() {
        val locationTask = LocationTask(object : LocationTask.LocationCallback {
            override fun onLocationFetched(latitude: Double, longitude: Double) {
                callAPIServiceNextDays(latitude, longitude)
            }
            override fun onLocationFetchError(errorMessage: String) {
            }
        })
        locationTask.execute()
    }
    private fun callAPIServiceNextDays(lat: Double, lon: Double) {
        val daily =
            listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset")
        if (InternetConnection.isNetworkAvailable(this)) {
            lifecycleScope.launch {
                nextDaysViewModel.getTheWeather(
                    lat,
                    lon,
                    daily,
                    TimeZone.getDefault().id,
                    Constants.NEXT7DAYS_WEATHER_DAYS
                )
            }
        } else {
            Log.d(
                "NextDaysFragment",
                "callingNext7DaysWeatherAPI: No internet connection, showing snackbar"
            )
            val snackBar =
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "No internet connection.",
                    Snackbar.LENGTH_INDEFINITE
                )
            snackBar.setAction(R.string.app_name) {
                if (InternetConnection.isNetworkAvailable(this)) {
                    lifecycleScope.launch {
                        nextDaysViewModel.getTheWeather(
                            lat,
                            lon,
                            daily,
                            TimeZone.getDefault().id,
                            7
                        )
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
    // Preparing daysData
    private fun sendNextDaysDataToUI(nextDaysData: Daily): List<DayItem> {
        // For Day0
        val temperaturMaxDay0 = (nextDaysData.temperature_2m_max[0]).toString()
        val temperatureMinDay0 = (nextDaysData.temperature_2m_min[0]).toString()
        val dateDay0 = Change.formatDates(nextDaysData.sunrise[0])
        val iconetypeDay0 = nextDaysData.weathercode[0]
        // For Day1
        val temperaturMaxDay1 = (nextDaysData.temperature_2m_max[1]).toString()
        val temperatureMinDay1 = (nextDaysData.temperature_2m_min[1]).toString()
        val dateDay1 = Change.formatDates(nextDaysData.sunrise[1])
        val iconetypeDay1 = nextDaysData.weathercode[1]
        // Day2
        val temperaturMaxDay2 = (nextDaysData.temperature_2m_max[2]).toString()
        val temperatureMinDay2 = (nextDaysData.temperature_2m_min[2]).toString()
        val dateDay2 = Change.formatDates(nextDaysData.sunrise[2])
        val iconetypeDay2 = nextDaysData.weathercode[2]
        //Day3
        val temperaturMaxDay3 = (nextDaysData.temperature_2m_max[3]).toString()
        val temperatureMinDay3 = (nextDaysData.temperature_2m_min[3]).toString()
        val dateDay3 = Change.formatDates(nextDaysData.sunrise[3])
        val iconetypeDay3 = nextDaysData.weathercode[3]
        //Day4
        val temperaturMaxDay4 = (nextDaysData.temperature_2m_max[4]).toString()
        val temperatureMinDay4 = (nextDaysData.temperature_2m_min[4]).toString()
        val dateDay4 = Change.formatDates(nextDaysData.sunrise[4])
        val iconetypeDay4 = nextDaysData.weathercode[4]
        //Day5
        val temperaturMaxDay5 = (nextDaysData.temperature_2m_max[5]).toString()
        val temperatureMinDay5 = (nextDaysData.temperature_2m_min[5]).toString()
        val dateDay5 = Change.formatDates(nextDaysData.sunrise[5])
        val iconetypeDay5 = nextDaysData.weathercode[5]
        //Day6
        val temperaturMaxDay6 = (nextDaysData.temperature_2m_max[6]).toString()
        val temperatureMinDay6 = (nextDaysData.temperature_2m_min[6]).toString()
        val dateDay6 = Change.formatDates(nextDaysData.sunrise[6])
        val iconetypeDay6 = nextDaysData.weathercode[6]
        // Return result to DaysAdapter
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
    //Weather Day Implementation
    private fun getWeatherDayLocalData(): DataWeather? {
        return weatherDayLocalDataViewModel.getData()
    }
    private fun initWeatherDayLocalData() {
        val service = WeatherDayLocalData(this)
        val repository = WeatherDayLocalDataRepository(service)
        weatherDayLocalDataViewModel = ViewModelProvider(
            this,
            WeatherDayLocalDataViewModelFactory(repository)
        )[WeatherDayLocalDataViewModel::class.java]
    }
    private fun initWeatherDayViewModel() {
        val service = RetrofitObject.getInstance(Constants.OpenWeatherMap_API_BASE_URL).create(
            WeatherApiService::class.java
        )
        val repository = WeatherRepository(service)
        weatherViewModel =
            ViewModelProvider(this, WeatherFactory(repository))[WeatherViewModel::class.java]
    }
    private fun callAPIServiceWeatherDay(lat: Double, lon: Double) {

        if (InternetConnection.isNetworkAvailable(this)) {
            lifecycleScope.launch {
                weatherViewModel.getTheWeather(lat, lon, Constants.OpenWeatherMap_api_key)
            }
        } else {
            Log.d(
                "NextDaysFragment",
                "callingNext7DaysWeatherAPI: No internet connection, showing snackbar"
            )
            val snackBar =
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "No internet connection.",
                    Snackbar.LENGTH_INDEFINITE
                )
            snackBar.setAction(R.string.app_name) {
                if (InternetConnection.isNetworkAvailable(this)) {

                    lifecycleScope.launch {
                        weatherViewModel.getTheWeather(lat, lon, Constants.OpenWeatherMap_api_key)
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
   // Save weather day data to db
    private fun sendDatatoDb(dataWeather: DataWeather) {
        weatherDayLocalDataViewModel.sendData(dataWeather)
    }
    // Get weather by actual location
    private fun getWeatherByCurrentLocation() {
        val locationTask = LocationTask(object : LocationTask.LocationCallback {
            override fun onLocationFetched(latitude: Double, longitude: Double) {
                Log.e("Latitude", "LAtitude: $latitude")
                callAPIServiceWeatherDay(latitude, longitude)
            }
            override fun onLocationFetchError(errorMessage: String) {
            }
        })
        locationTask.execute()
    }
    @SuppressLint("SetTextI18n")
    private fun sendWeatherDatatoUi(weatherData: DataWeather) {
        //Temperature TO celsius
        val temperatureCelsius =
            (weatherData.main?.temp?.let { Change.kelvinToCelsius(it) })?.toInt()
        //Temperature Max
        val temperatureCelsiusMax =
            (weatherData.main?.tempMax.let { Change.kelvinToCelsius(it!!) }).toInt()
       // Temperature Min
        val temperatureCelsiusMin =
            (weatherData.main?.tempMin?.let { Change.kelvinToCelsius(it) })?.toInt()
        // Wind Speed in Km/h
        val windSpeedKmPerHour =
            (weatherData.wind?.speed?.let { Change.metersPerSecondToKmPerHour(it) })?.toInt()
        // Sunrise
        val sunriseTime =
            weatherData.sys?.sunrise?.let { Change.unixTimestampToLocalTime(it.toLong()) }
        // Sunset
        val sunsetTime =
            weatherData.sys?.sunset?.let { Change.unixTimestampToLocalTime(it.toLong()) }
        val readableDate = weatherData.dt?.let { Change.timestampToReadableDate(it.toLong()) }
        // Humidity
        val humidity = weatherData.main?.humidity
        // Pressure
        val pressure = weatherData.main?.pressure
        // Country
        val country = weatherData.name
        val infotext = weatherData.weather[0].description
        // Preparing to send data to UI
        val countryTextView: TextView = findViewById(R.id.contry_text_view)
        val dateTextView: TextView = findViewById(R.id.date_text_view)
        val tempTextView: TextView = findViewById(R.id.temp_text_view)
        val infoTextView: TextView = findViewById(R.id.info_text_view)
        val tempMinTextView: TextView = findViewById(R.id.tempMin_text_view)
        val tempMaxTextView: TextView = findViewById(R.id.tempMax_text_view)
        val sunriseTextView: TextView = findViewById(R.id.sunrise_text_view)
        val sunsetTextView: TextView = findViewById(R.id.sunset_text_view)
        val windTextView: TextView = findViewById(R.id.wind_text_view)
        val humidityTextView: TextView = findViewById(R.id.humidity_text_view)
        val pressureTextView: TextView = findViewById(R.id.pressure_text_view)
         // Send data to UI
        countryTextView.text = country
        dateTextView.text = readableDate
        tempTextView.text = "$temperatureCelsius°C"
        infoTextView.text = infotext
        tempMinTextView.text = "$temperatureCelsiusMin°C"
        tempMaxTextView.text = "$temperatureCelsiusMax°C"
        sunriseTextView.text = sunriseTime
        sunsetTextView.text = sunsetTime
        windTextView.text = "$windSpeedKmPerHour km/h"
        humidityTextView.text = "$humidity%"
        pressureTextView.text = "$pressure"
    }
    // Get lan lon of the searched city
    private fun callAPIToGetLocation(cityName: String) {
        lifecycleScope.launch {
            val service = RetrofitObject.getInstance(Constants.OpenWeatherMap_API_BASE_URL).create(
                SearchService::class.java
            )
            val repository = SearchRepository(service)
            val locationPair = repository.getLocation(cityName, Constants.OpenWeatherMap_api_key)
            if (locationPair != null) {
                // Mettez à jour les variables de latitude et de longitude
                searchedLat = locationPair.first
                searchedLon = locationPair.second
                // Appelez ensuite les fonctions pour obtenir les données météorologiques actuelles
                Log.d("CitynameInside API ","city ! $cityName" )
                Log.d("searchedlatlon", "lat & lon  = $searchedLon , $searchedLat")
            } else {
                 Log.d("searchedlatlon" ," lat & lon = NULL")
            }
        }
    }
}

