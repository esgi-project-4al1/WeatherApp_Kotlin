package com.example.app_meteo.vue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_meteo.R
import com.example.app_meteo.model.nextDaysModel.Daily
/*

class AdapterNextdays  (private val weatherData: Daily) :
       RecyclerView.Adapter<AdapterNextdays.ViewHolder>() {

    /*    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Define TextViews for day, temperature, sunrise, sunset, etc.
            val textViewDay: TextView = itemView.findViewById(R.id.textViewDay1)
            val textViewTemperature: TextView = itemView.findViewById(R.id.textViewTemperature1)
            val textViewSunrise: TextView = itemView.findViewById(R.id.textViewSunrise1)
            val textViewSunset: TextView = itemView.findViewById(R.id.textViewSunset1)
        } */

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.seven_days_layout, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.textViewDay.text = "Day ${position + 1}"
            holder.textViewTemperature.text = "${weatherData.temperature_2m_max[position].toInt()}/${weatherData.temperature_2m_min[position].toInt()}"
            holder.textViewSunrise.text = "Sunrise: ${weatherData.sunrise[position]}"
            holder.textViewSunset.text = "Sunset: ${weatherData.sunset[position]}"
        }

        override fun getItemCount(): Int {
            // Return the size of the data set (number of days)
            return weatherData.time.size

        }


    }
*/
