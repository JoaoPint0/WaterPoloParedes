package com.endeavour.poloaquaticoparedes.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository
import com.endeavour.poloaquaticoparedes.ui.game.GameViewModel

class GameViewModelFactory(private val repository: WaterPoloRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}