package com.example.app_meteo.vue

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_meteo.R
import com.example.app_meteo.model.modelSimpleDays.DayItem
import com.example.app_meteo.utils.Change

class DaysAdapter(private val daysList: List<DayItem>) :
    RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.day_days_text_view)
        val tempMinTextView: TextView = itemView.findViewById(R.id.temp_min_text_view)
        val tempMaxTextView: TextView = itemView.findViewById(R.id.temp_max_text_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seven_days_layout, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayItem = daysList[position]
        if(position == 0){
            holder.dayTextView.text = "Auj."
            holder.tempMinTextView.text = dayItem.tempMin
            holder.tempMaxTextView.text = dayItem.tempMax
        }else{


            holder.dayTextView.text = Change.getDayOfWeekFromString(dayItem.dayName)
            holder.tempMinTextView.text = dayItem.tempMin
            holder.tempMaxTextView.text = dayItem.tempMax
        }




    }

    override fun getItemCount(): Int {
        return daysList.size
    }
}