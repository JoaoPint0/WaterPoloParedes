package com.endeavour.poloaquaticoparedes.ui.athletes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class AthletesViewModelFactory(private val repository: WaterPoloRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AthletesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AthletesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}