package com.example.app_meteo.vue

import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.app_meteo.R
    import com.example.app_meteo.viewmodel.NextDaysViewModel
    import com.example.app_meteo.data.RetrofitObject
    import com.example.app_meteo.data.local.NextDaysLocalData
    import com.example.app_meteo.data.remote.NextdaysService
    import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
    import com.example.app_meteo.data.repository.NextdaysRepository
    import com.example.app_meteo.model.nextDaysModel.NextDays
    import kotlinx.coroutines.launch
    import com.example.app_meteo.utils.Constants.OpenMeteo_API_BASE_URL
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModel
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModelFactory
    import com.example.app_meteo.viewmodel.NextDaysViewModelFactory
    import com.google.android.material.snackbar.Snackbar
    import java.util.TimeZone
    import com.example.app_meteo.vue.InternetConnection

    import com.example.app_meteo.databinding.NextdaysBinding  // Import the generated binding class
    import com.example.app_meteo.vue.TimeUtil

    class NextDaysFragment : Fragment() {


        private lateinit var nextDaysLocalDataViewModel: NextDaysLocalDataViewModel
        private lateinit var nextDaysViewModel: NextDaysViewModel
        private lateinit var recyclerView: RecyclerView


        private lateinit var binding: NextdaysBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = NextdaysBinding.inflate(inflater, container, false)
            return binding.root
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


            val lat = "48.8566"
            val lon = "2.3522"
            print("HEY im here")

            initNext7DaysWeather()
            initUpcomingDaysSharedPref()

            callingNext7DaysWeatherAPI(lat, lon)

            val weatherData = getNextDaysLocalData()
            recyclerView = binding.recyclerViewWeather
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            Log.d("NextDaysFragment", "onViewCreated: weatherData: $weatherData")


            if(weatherData != null) {
                val adapter = AdapterNextdays(weatherData!!)
                recyclerView.adapter = adapter
                setNext7DaysWeatherToUI(weatherData)

            }
            else{
                Log.d("NextDaysFragment", "onViewCreated: weatherData: $weatherData")

            }


            nextDaysViewModel.weatherLiveData.observe(requireActivity()) {
                if(it != null) {
                    Log.d("NextDaysFragment", "Observer triggered with data: $it")
                    setNext7DaysWeatherToUI(it.daily)
                    sendDataToSharedPref(it.daily)
                }
            }
            Log.d("NextDaysFragment", "NextDaysLocalDataViewModel: $nextDaysLocalDataViewModel")
            Log.d("NextDaysFragment", "Local Data: ${getNextDaysLocalData()}")
        }



        private fun getNextDaysLocalData(): NextDays? {
            Log.d("NextDaysFragment", "Getdata")

            return nextDaysLocalDataViewModel.getData()
        }

        private fun initUpcomingDaysSharedPref() {
            val service = NextDaysLocalData(requireActivity())
            val repository = NextDaysLocalDataRepository(service)

            nextDaysLocalDataViewModel = ViewModelProvider(
                requireActivity(),
                NextDaysLocalDataViewModelFactory(repository)
            )[NextDaysLocalDataViewModel::class.java]
        }


        private fun initNext7DaysWeather() {
            val service = RetrofitObject.getInstance(OpenMeteo_API_BASE_URL).create(
                NextdaysService::class.java
            )
            val repository = NextdaysRepository(service)

            nextDaysViewModel = ViewModelProvider(requireActivity(), NextDaysViewModelFactory(repository))[NextDaysViewModel::class.java]
            Log.d("NextDaysFragment", "Nextdaysmodel: Model provider")

        }


        private fun callingNext7DaysWeatherAPI(lat: String, lon: String) {
            val dailyParameters = listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset")

            if(InternetConnection.isNetworkAvailable(requireActivity())) {
                Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: Network available, launching coroutine")

                lifecycleScope.launch {


                    nextDaysViewModel.getTheWeather(lat, lon, dailyParameters, TimeZone.getDefault().id, 6)
                }
            } else {
                Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: No internet connection, showing snackbar")

                val snackBar =
                    Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(R.string.app_name) {
                    if (InternetConnection.isNetworkAvailable(requireActivity())) {
                        Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: Internet available after snackbar action, launching coroutine")

                        lifecycleScope.launch {
                            nextDaysViewModel.getTheWeather(lat, lon, dailyParameters, TimeZone.getDefault().id, 7)
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

        private fun sendDataToSharedPref(weatherData: NextDays) {
            nextDaysLocalDataViewModel.sendData(weatherData)
        }

        private fun setNext7DaysWeatherToUI(weather: NextDays) {
         /*   val tomorrowTemp = "${weather.temperature_max[1].toInt()}/${weather.temperature_min[1].toInt()}"
            binding.textViewTemperature1.text = tomorrowTemp
            binding.textViewSunrise1.text = TimeUtil.extractTimeFromString(weather.sunrise[1])
            binding.textViewSunset1.text = TimeUtil.extractTimeFromString(weather.sunset[1])

            // Day 2
            val day2Temp = "${weather.temperature_min[2].toInt()}/${weather.temperature_max[2].toInt()}"
            binding.textViewTemperature2.text = day2Temp
            binding.textViewDay2.text = TimeUtil.convertDateStringToDay(weather.time[2])

            // Day 3
            val day3Temp = "${weather.temperature_min[3].toInt()}/${weather.temperature_max[3].toInt()}"
            binding.textViewTemperature3.text = day3Temp
            binding.textViewDay3.text = TimeUtil.convertDateStringToDay(weather.time[3])

            // Day 4
            val day4Temp = "${weather.temperature_min[4].toInt()}/${weather.temperature_max[4].toInt()}"
            binding.textViewTemperature4.text = day4Temp
            binding.textViewDay4.text = TimeUtil.convertDateStringToDay(weather.time[4])

            // Day 5
            val day5Temp = "${weather.temperature_min[5].toInt()}/${weather.temperature_max[5].toInt()}"
            binding.textViewTemperature5.text = day5Temp
            binding.textViewDay5.text = TimeUtil.convertDateStringToDay(weather.time[5])

            // Day 6
            val day6Temp = "${weather.temperature_min[6].toInt()}/${weather.temperature_max[6].toInt()}"
            binding.textViewTemperature6.text = "55"
            binding.textViewDay6.text = TimeUtil.convertDateStringToDay(weather.time[6])

    */
            val adapter = AdapterNextdays(weather)

            recyclerView.adapter = adapter

        }

        private fun printAllWeatherData(weather: NextDays) {
            // Assurez-vous que les listes ont au moins 7 éléments
            if (weather.temperature_max.size < 7 ||
                weather.temperature_min.size < 7 ||
                weather.sunrise.size < 7 ||
                weather.sunset.size < 7 ||
                weather.time.size < 7
            ) {
                Log.d("WeatherData", "Les données ne sont pas complètes pour les 7 jours.")
                return
            }

            for (i in 1..7) {
                Log.d("WeatherData", "Jour $i:")
                Log.d("WeatherData", "   Temperature Max: ${weather.temperature_max[i].toInt()}")
                Log.d("WeatherData", "   Temperature Min: ${weather.temperature_min[i].toInt()}")
                Log.d("WeatherData", "   Sunrise: ${TimeUtil.extractTimeFromString(weather.sunrise[i])}")
                Log.d("WeatherData", "   Sunset: ${TimeUtil.extractTimeFromString(weather.sunset[i])}")
                Log.d("WeatherData", "   Time: ${TimeUtil.convertDateStringToDay(weather.time[i])}")
                Log.d("WeatherData", "------------")
            }
        }

    }


