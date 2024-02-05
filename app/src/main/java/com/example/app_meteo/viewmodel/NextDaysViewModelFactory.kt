package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_meteo.data.repository.NextdaysRepository

class NextDaysViewModelFactory( private val  repository: NextdaysRepository) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NextDaysViewModel(repository) as T
    }

}