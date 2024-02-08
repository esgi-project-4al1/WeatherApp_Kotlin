package com.example.app_meteo.vue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.app_meteo.R
import com.example.app_meteo.data.RetrofitObject
import com.example.app_meteo.data.remote.WeatherApiService
import com.example.app_meteo.data.repository.CityRepository
import com.example.app_meteo.data.repository.WeatherRepository
import com.example.app_meteo.model.modelSimpleDays.DataWeather
import com.example.app_meteo.utils.Constants
import com.example.app_meteo.utils.Constants.OpenWeatherMap_api_key
import com.example.app_meteo.viewmodel.CityViewModel
import com.example.app_meteo.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitySearchFragment<CityData> : Fragment() {

    private lateinit var cityViewModel: CityViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    private lateinit var cityEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var weatherDisplayTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city_search, container, false)

        cityEditText = view.findViewById(R.id.search_city)
        searchButton = view.findViewById(R.id.search_button)
        weatherDisplayTextView = view.findViewById(R.id.weather_display)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        setupSearchButton()
    }

    private fun initViewModels() {
        // Initialisation de CityViewModel
        val cityService = RetrofitObject.getInstance(Constants.OpenWeatherMap_API_BASE_URL).create(WeatherApiService::class.java)

        // Initialisation de WeatherViewModel
        val weatherService = RetrofitObject.getInstance(Constants.OpenWeatherMap_API_BASE_URL).create(WeatherApiService::class.java)
        val weatherRepository = WeatherRepository(weatherService)
    }

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val cityName = cityEditText.text.toString().trim()
            if (cityName.isNotEmpty()) {
                performCitySearch(cityName)
            } else {
                Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performCitySearch(cityName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cityViewModel.searchCity(cityName)
        }
        // Observe city search results and update UI accordingly
        cityViewModel.citySearchResults.observe(viewLifecycleOwner, Observer { cities ->
            // Assuming you want to display weather data for the first city in the result
            cities.firstOrNull()?.let { city ->

            }
        })
    }



    private fun updateWeatherDisplay(weatherData: DataWeather) {
        // Update weather display with data
        val displayText = "Temperature: ${weatherData.main?.temp}°C\n" +
                "Weather: ${weatherData.weather.firstOrNull()?.description}"
        weatherDisplayTextView.text = displayText
    }
}
