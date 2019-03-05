package com.endeavour.poloaquaticoparedes.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.endeavour.poloaquaticoparedes.model.Athlete
import com.endeavour.poloaquaticoparedes.model.Event
import java.util.concurrent.Executor

class WaterPoloLocalCache(
        private val athleteDao: AthleteDao,
        private val eventDao: EventDao,
        private val ioExecutor: Executor
) {

    fun insertAthletes(athletes: List<Athlete>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${athletes.size} athletes")
            athleteDao.insert(athletes)
            insertFinished()
        }
    }

    fun athletesByNameCardIdOrLevel(name: String): LiveData<List<Athlete>> {
        val query = "%${name.replace(' ', '%')}%"
        return athleteDao.athletesByNameCardIdOrLevel(query)
    }

    fun athleteByCardId(id: Long): LiveData<Athlete> {
        return athleteDao.athleteByCardId(id)
    }

    fun allAthletes(): LiveData<List<Athlete>> {
        return athleteDao.allAthletes()
    }

    fun updateAthlete(athlete: Athlete) {
        ioExecutor.execute {
            athleteDao.updateAthlete(athlete)
        }
    }

    fun deleteAthlete(cardId: String) {

        ioExecutor.execute {
            athleteDao.deleteAthlete(cardId)
        }
    }

    fun deleteAllAthletes() {
        ioExecutor.execute {
            athleteDao.deleteAll()
        }
    }



    fun insertEvents(events: List<Event>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${events.size} events")
            eventDao.insert(events)
            insertFinished()
        }
    }

    fun eventsByName(name: String): LiveData<List<Event>> {
        val query = "%${name.replace(' ', '%')}%"
        return eventDao.eventsByName(query)
    }

    fun getEventById(id: Long) : LiveData<Event>{

        return eventDao.eventById(id)
    }

    fun allEvents(): LiveData<List<Event>> {
        return eventDao.allEvents()
    }


    fun updateEvent(event: Event) {
        ioExecutor.execute {
            eventDao.updateEvent(event)
        }
    }

    fun deleteEvent(id: Long) {

        ioExecutor.execute {
            eventDao.deleteEvent(id)
        }
    }

    fun deleteAllEvents() {
        ioExecutor.execute {
            eventDao.deleteAll()
        }
    }

}