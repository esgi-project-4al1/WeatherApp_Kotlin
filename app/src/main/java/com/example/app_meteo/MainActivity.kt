package com.example.app_meteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app_meteo.vue.NextDaysFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NextDaysFragment())
            .commit()
    }
}