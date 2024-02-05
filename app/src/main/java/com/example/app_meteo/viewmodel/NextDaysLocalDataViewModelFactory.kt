package com.example.app_meteo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_meteo.data.repository.NextDaysLocalDataRepository

class NextDaysLocalDataViewModelFactory(private val repository: NextDaysLocalDataRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NextDaysLocalDataViewModel(repository) as T
    }
}