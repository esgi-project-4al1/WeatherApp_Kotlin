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
    import com.example.app_meteo.data.db.NextDaysLocalData
    import com.example.app_meteo.data.services.NextdaysService
    import com.example.app_meteo.data.repository.NextDaysLocalDataRepository
    import com.example.app_meteo.data.repository.NextdaysRepository
import com.example.app_meteo.model.modelSimpleDays.DayItem
import kotlinx.coroutines.launch
    import com.example.app_meteo.utils.Constants.OpenMeteo_API_BASE_URL
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModel
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModelFactory
    import com.example.app_meteo.viewmodel.NextDaysViewModelFactory
    import com.google.android.material.snackbar.Snackbar
    import java.util.TimeZone

    import com.example.app_meteo.model.nextDaysModel.Daily
    import com.example.app_meteo.utils.Constants
    import com.example.app_meteo.utils.Change

class NextDaysFragment : Fragment() {


        private lateinit var nextDaysLocalDataViewModel: NextDaysLocalDataViewModel
        private lateinit var nextDaysViewModel: NextDaysViewModel
        private lateinit var recyclerView: RecyclerView




        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return inflater.inflate(R.layout.activity_main, container, false)

        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

              // Manque function qui change date to string
             // mangque fonction qui renvoi lat et lon
            // fonction qui set data to UI
            // UI for 7 days

            val lat = "48.8566"
            val lon = "2.3522"
            print("HEY im here")

            initNextDaysViewModel()
            initNextDaysLocalData()

            callAPIServiceNextDays(lat, lon)

            val nextDaysData = getNextDaysLocalData()
            // recyclerView = binding.recyclerViewWeather
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            Log.d("NextDaysFragment", "onViewCreated: nextDaysData: $nextDaysData")


            if(nextDaysData != null) {

              //  setNext7DaysWeatherToUI(nextDaysData)

            }
            else{
                Log.d("NextDaysFragment", "onViewCreated: nextDaysData: $nextDaysData")

            }


            nextDaysViewModel.weatherLiveData.observe(requireActivity()) {
                if(it != null) {
                    Log.d("NextDaysFragment", "Observer triggered with data: $it")
                    if(it.daily != null) {
                      //  setNext7DaysWeatherToUI(it.daily!!)
                        sendDatatoDb(it.daily!!)
                    }
                    else {
                        Log.d("id.daily", "Null")
                    }
                }
            }
            Log.d("NextDaysFragment", "NextDaysLocalDataViewModel: $nextDaysLocalDataViewModel")
            Log.d("NextDaysFragment", "Local Data: ${getNextDaysLocalData()}")
        }



        private fun getNextDaysLocalData(): Daily? {

            return nextDaysLocalDataViewModel.getData()
        }

        private fun initNextDaysLocalData() {
            val service = NextDaysLocalData(requireActivity())
            val repository = NextDaysLocalDataRepository(service)

            nextDaysLocalDataViewModel = ViewModelProvider(
                requireActivity(),
                NextDaysLocalDataViewModelFactory(repository)
            )[NextDaysLocalDataViewModel::class.java]
        }


        private fun initNextDaysViewModel() {
            val service = RetrofitObject.getInstance(OpenMeteo_API_BASE_URL).create(
                NextdaysService::class.java
            )
            val repository = NextdaysRepository(service)

            nextDaysViewModel = ViewModelProvider(requireActivity(), NextDaysViewModelFactory(repository))[NextDaysViewModel::class.java]

        }


        private fun callAPIServiceNextDays(lat: String, lon: String) {
            val daily = listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset")

            if(InternetConnection.isNetworkAvailable(requireActivity())) {

                lifecycleScope.launch {
                    nextDaysViewModel.getTheWeather(lat, lon, daily, TimeZone.getDefault().id, Constants.NEXT7DAYS_WEATHER_DAYS)
                }
            } else {
                Log.d("NextDaysFragment", "callingNext7DaysWeatherAPI: No internet connection, showing snackbar")

                val snackBar =
                    Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(R.string.app_name) {
                    if (InternetConnection.isNetworkAvailable(requireActivity())) {

                        lifecycleScope.launch {
                            nextDaysViewModel.getTheWeather(lat, lon, daily, TimeZone.getDefault().id, 7)
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

        private fun sendDatatoDb(nextDaysData: Daily) {
            nextDaysLocalDataViewModel.sendData(nextDaysData)
        }




      private fun dataitem(nextDaysData:    Daily): List<DayItem> {
          // Day 1
          // icons 0 or 100 == sunny
          //  1 to 50 == cloud sunny
          // 51  to 99 = cloudy

          val temperaturMaxDay1 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[1]).toString()
          val temperatureMinDay1 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[1]).toString()
          val dateDay1 = Change.formatDates(nextDaysData.sunrise[1]).toString()
          val iconetypeDay1 = Change.weatherCodetoIcon(nextDaysData.weathercode[1])


          // Day2
          val temperaturMaxDay2 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[2]).toString()
          val temperatureMinDay2 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[2]).toString()
          val dateDay2 = Change.formatDates(nextDaysData.sunrise[2])
          val iconetypeDay2 = Change.weatherCodetoIcon(nextDaysData.weathercode[2])


          //Day3
          val temperaturMaxDay3 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[3]).toString()
          val temperatureMinDay3 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[3]).toString()
          val dateDay3 = Change.formatDates(nextDaysData.sunrise[3])
          val iconetypeDay3 = Change.weatherCodetoIcon(nextDaysData.weathercode[3])


          //Day4
          val temperaturMaxDay4 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[4]).toString()
          val temperatureMinDay4 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[4]).toString()
          val dateDay4 = Change.formatDates(nextDaysData.sunrise[4])
          val iconetypeDay4 = Change.weatherCodetoIcon(nextDaysData.weathercode[4])


          //Day5
          val temperaturMaxDay5 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[5]).toString()
          val temperatureMinDay5 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[5]).toString()
          val dateDay5 = Change.formatDates(nextDaysData.sunrise[5])
          val iconetypeDay5 = Change.weatherCodetoIcon(nextDaysData.weathercode[5])


          //Day6
          val temperaturMaxDay6 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[6]).toString()
          val temperatureMinDay6 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[6]).toString()
          val dateDay6 = Change.formatDates(nextDaysData.sunrise[6])
          val iconetypeDay6 = Change.weatherCodetoIcon(nextDaysData.weathercode[6])


          //Day7
          val temperaturMaxDay7 = Change.kelvinToCelsius(nextDaysData.temperature_2m_max[7]).toString()
          val temperatureMinDay7 = Change.kelvinToCelsius(nextDaysData.temperature_2m_min[7]).toString()
          val dateDay7 = Change.formatDates(nextDaysData.sunrise[7])
          val iconetypeDay7 = Change.weatherCodetoIcon(nextDaysData.weathercode[7])




          return listOf(
              DayItem(dateDay1, iconetypeDay1, temperatureMinDay1, temperaturMaxDay1),
              DayItem(dateDay2, iconetypeDay2, temperatureMinDay2, temperaturMaxDay2),
              DayItem(dateDay3, iconetypeDay3, temperatureMinDay3, temperaturMaxDay3),
              DayItem(dateDay4, iconetypeDay4, temperatureMinDay4, temperaturMaxDay4),
              DayItem(dateDay5, iconetypeDay5, temperatureMinDay5, temperaturMaxDay5),
              DayItem(dateDay6, iconetypeDay6, temperatureMinDay6, temperaturMaxDay6),
              DayItem(dateDay7, iconetypeDay7, temperatureMinDay7, temperaturMaxDay7)
          )
      }



        }




