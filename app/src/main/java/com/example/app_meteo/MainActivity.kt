package com.example.app_meteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_meteo.modelSimpleDays.DayItem
import com.example.app_meteo.modelSimpleDays.DaysAdapter
import com.example.app_meteo.viewmodel.WeatherDaysViewModel
import kotlin.math.log

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

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val daysList = listOf(
            DayItem("Lun", "icone1", "10°C", "20°C"),
            DayItem("Mar", "icone2", "12°C", "22°C"),
            DayItem("Mer", "icone3", "11°C", "21°C"),
            DayItem("Jeu", "icone4", "9°C", "19°C"),
            DayItem("Ven", "icone5", "8°C", "18°C"),
            DayItem("Sam", "icone6", "7°C", "17°C"),
            DayItem("Dim", "icone7", "6°C", "16°C")
        )


        val adapter = DaysAdapter(daysList)
        Log.e("taille", "${adapter.itemCount}")
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        weatherViewModel.getWeatherByCurrentLocation()
    }
}
