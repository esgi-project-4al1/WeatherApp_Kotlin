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
    import kotlinx.coroutines.launch
    import com.example.app_meteo.utils.Constants.OpenMeteo_API_BASE_URL
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModel
    import com.example.app_meteo.viewmodel.NextDaysLocalDataViewModelFactory
    import com.example.app_meteo.viewmodel.NextDaysViewModelFactory
    import com.google.android.material.snackbar.Snackbar
    import java.util.TimeZone

import com.example.app_meteo.databinding.NextdaysBinding  // Import the generated binding class
import com.example.app_meteo.model.nextDaysModel.Daily
import com.example.app_meteo.utils.Constants

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
            recyclerView = binding.recyclerViewWeather
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            Log.d("NextDaysFragment", "onViewCreated: nextDaysData: $nextDaysData")


            if(nextDaysData != null) {
                val adapter = AdapterNextdays(nextDaysData)
                recyclerView.adapter = adapter
                setNext7DaysWeatherToUI(nextDaysData)

            }
            else{
                Log.d("NextDaysFragment", "onViewCreated: nextDaysData: $nextDaysData")

            }


            nextDaysViewModel.weatherLiveData.observe(requireActivity()) {
                if(it != null) {
                    Log.d("NextDaysFragment", "Observer triggered with data: $it")
                    if(it.daily != null) {
                        setNext7DaysWeatherToUI(it.daily!!)
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

        private fun setNext7DaysWeatherToUI(weather: Daily) {

            val adapter = AdapterNextdays(weather)

            recyclerView.adapter = adapter

        }


        }




