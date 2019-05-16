package com.endeavour.poloaquaticoparedes

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.endeavour.poloaquaticoparedes.api.WaterPoloService
import com.endeavour.poloaquaticoparedes.database.*
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository
import com.endeavour.poloaquaticoparedes.ui.athletes.AthletesViewModelFactory
import com.endeavour.poloaquaticoparedes.ui.event.EventViewModelFactory
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModelFactory
import java.util.concurrent.Executors

object Injection {

    private fun provideCache(context: Context): WaterPoloLocalCache {
        val athletesDB = PoloDatabase.getInstance(context)
        val eventsDB = PoloDatabase.getInstance(context)
        val gamesDB = PoloDatabase.getInstance(context)

        return WaterPoloLocalCache(athletesDB.athleteDao(), eventsDB.eventDao(),gamesDB.teamDao(), Executors.newSingleThreadExecutor())
    }

    fun provideRepository(context: Context): WaterPoloRepository {
        return WaterPoloRepository.getInstance(WaterPoloService.create(context), provideCache(context))
    }

    fun provideAthletesViewModelFactory(context: Context): ViewModelProvider.Factory {
        return AthletesViewModelFactory(provideRepository(context))
    }

    fun provideEventsViewModelFactory(context: Context): ViewModelProvider.Factory {
        return EventViewModelFactory(provideRepository(context))
    }

    fun provideGameViewModelFactory(context: Context): ViewModelProvider.Factory {
        return GameViewModelFactory(provideRepository(context))
    }
}