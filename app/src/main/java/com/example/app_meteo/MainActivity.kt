package com.example.app_meteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.app_meteo.viewmodel.WeatherDaysViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherDaysViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherViewModel = ViewModelProvider(this).get(WeatherDaysViewModel::class.java)


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


        weatherViewModel.initViews(
            countryTextView,
            dateTextView,
            tempTextView,
            infoTextView,
            tempMinTextView,
            tempMaxTextView,
            sunriseTextView,
            sunsetTextView,
            windTextView,
            humidityTextView,
            pressureTextView
        )

        Log.e("WeatherViewModel", "je vais lancer ")
        weatherViewModel.getWeatherByCurrentLocation()
    }
}
