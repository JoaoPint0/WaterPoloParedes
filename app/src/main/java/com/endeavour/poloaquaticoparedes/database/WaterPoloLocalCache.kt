package com.endeavour.poloaquaticoparedes.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.endeavour.poloaquaticoparedes.model.Athlete
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.Team
import java.util.concurrent.Executor

class WaterPoloLocalCache(
        private val athleteDao: AthleteDao,
        private val eventDao: EventDao,
        private val teamDao: TeamDao,
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

    fun athleteByCardId(id: Long) = athleteDao.athleteByCardId(id)
    fun allAthletes() = athleteDao.allAthletes()

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

    fun allEvents() = eventDao.allEvents()
    fun eventsById(id: Long) = eventDao.eventById(id)

    fun eventsByName(name: String): LiveData<List<Event>> {
        val query = "%${name.replace(' ', '%')}%"
        return eventDao.eventsByName(query)
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



    fun insertTeams(teams: List<Team>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${teams.size} events")
            teamDao.insert(teams)
            insertFinished()
        }
    }

    fun allTeams() = teamDao.allTeams()

    fun deleteAllTeams() {
        ioExecutor.execute {
            teamDao.deleteAll()
        }
    }

}