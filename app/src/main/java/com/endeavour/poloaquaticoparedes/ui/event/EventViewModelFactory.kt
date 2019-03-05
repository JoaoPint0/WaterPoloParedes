package com.endeavour.poloaquaticoparedes.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class EventViewModelFactory(private val repository: WaterPoloRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}