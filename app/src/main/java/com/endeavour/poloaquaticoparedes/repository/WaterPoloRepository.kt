package com.endeavour.poloaquaticoparedes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.endeavour.poloaquaticoparedes.api.*
import com.endeavour.poloaquaticoparedes.database.WaterPoloLocalCache
import com.endeavour.poloaquaticoparedes.model.*

class WaterPoloRepository(
        private val service: WaterPoloService,
        private val cache: WaterPoloLocalCache) {

    private val TAG = "WaterPoloRepository"

    fun searchAllAthletes(query: String): LiveData<List<Athlete>> {


        searchAllAthletes(service, {

            cache.deleteAllAthletes()
            cache.insertAthletes(it) {
            }
        }, {
            Log.e(TAG, "searchAllAthletes Error $it")
        })

        return if (query.isNotEmpty()) cache.athletesByNameCardIdOrLevel(query) else cache.allAthletes()
    }

    fun searchAthleteByCardId(cardId: String): LiveData<ComplexAthlete> {

        val athlete = MutableLiveData<ComplexAthlete>()

        searchAthleteByCardId(service, cardId, {
            athlete.value = it
            Log.e(TAG, "search ComplexAthlete  response")
        }, {
            Log.e(TAG, "search ComplexAthlete error $it")
        })

        return athlete
    }

    fun searchAthletePaymentBySeason(cardId: Long, year: Int): LiveData<List<Payment>> {

        val athlete = MutableLiveData<List<Payment>>()

        searchAthletePaymentBySeason(service, cardId, year, {
            athlete.value = it
            Log.e(TAG, "search athlete $cardId payments response")
        }, {
            Log.e(TAG, "search athlete $cardId payments error $it")
        })

        return athlete
    }

    fun createAthlete(athlete: ComplexAthlete): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        createAthlete(service, athlete, {

            if (it?.success == true) {

                val response = Athlete(athlete.name, athlete.gender, athlete.mobileNumber, athlete.email, athlete.cardId, athlete.level, athlete.enrolled)

                success.value = true

                cache.insertAthletes(listOf(response)) {
                    Log.e(TAG, "insert finished")
                }

            }else{
                success.value = false
            }

        }, {
            success.value = false
            Log.e(TAG, "create Athlete error $it")
        })

        return success
    }

    fun updateAthlete(athlete: ComplexAthlete): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        updateAthlete(service, athlete, {

            if(it?.success == true) {

                success.value = true
                val response = Athlete(athlete.name, athlete.gender,athlete.mobileNumber, athlete.email, athlete.cardId, athlete.level, athlete.enrolled)
                cache.updateAthlete(response)
            } else{
                success.value = false
            }
        }, {
            success.value = false
            Log.e(TAG, "update Athlete error $it")
        })

        return success
    }

    fun deleteAthlete(cardId: String): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        deleteAthlete(service,cardId, {


            Log.e(TAG, "delete Athlete $it")

            if(it?.success == true) {

                success.value = true
                cache.deleteAthlete(cardId)

            }else{
                success.value = false
            }

        }, {
            success.value = false
            Log.e(TAG, "update Athlete error $it")
        })

        return success
    }

    fun searchAllEvents(query: String): LiveData<List<Event>> {

        searchAllEvents(service, {
            Log.e(TAG, "search All Event  $it")
            cache.deleteAllEvents()
            cache.insertEvents(it) {}
        }, {
            Log.e(TAG, "search All Event Error $it")
        })

        return if (query.isNotEmpty()) cache.eventsByName(query) else cache.allEvents()
    }

    fun getEventById(id: Long): LiveData<Event> {

        val event = MutableLiveData<Event>()

        getEventById(service, id,{

            if (it != null){

                event.value = it
                cache.insertEvents(listOf(it)) {}
            }
        },{
            Log.e(TAG, "getEventById  $id  error $it")
        })

        return event
    }

    fun createEvent(event: Event): LiveData<ApiResponse> {

        val success = MutableLiveData<ApiResponse>()

        createEvent(service, event, {
            Log.e(TAG, "create Event  $it")
            success.value = it
            if (it?.success == true){

                event.id = it.id.toLong()
                cache.insertEvents(listOf(event)) {}
            }
        }, {
            success.value = ApiResponse("", false, "0")
            Log.e(TAG, "create Event error $it")
        })

        return success
    }

    fun editEvent(event: Event): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        editEvent(service, event, {
            Log.e(TAG, "edit Event  $it")
            if (it?.success == true){

                success.value = true
                cache.updateEvent(event)
            }else{
                success.value = false
            }
        }, {
            success.value = false
            Log.e(TAG, "create Event error $it")
        })

        return success
    }

    fun deleteEvent(id: Long): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        deleteEvent(service, id, {
            Log.e(TAG, "delte Event  $it")
            if (it?.success == true){

                success.value = true
                cache.deleteEvent(id)

            }else{
                success.value = false
            }
        }, {
            success.value = false
            Log.e(TAG, "create Event error $it")
        })

        return success
    }

    fun searchAllGames(): LiveData<List<Game>> {

        return CloudFireStore().getGames()
    }

    fun getGameById(id: String): LiveData<Game> {

        return CloudFireStore().getGameById(id)
    }

    fun createGame(game: Game): LiveData<ApiResponse> {

        return CloudFireStore().addGame(game)
    }

    fun editGame(game: Game): LiveData<ApiResponse> {

        return CloudFireStore().updateGame(game)
    }

    fun deleteGame(id: Long): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        return success
    }

    fun createPayment(payments: CreatePayment) :LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        createAthletePayments(service, payments,{

            success.value = it?.success

        },{
            success.value = false
            Log.e(TAG, "createPayment error $it")
        })

        return success
    }

    fun validateUser(loginObject: LoginUser): LiveData<Boolean> {

        val success = MutableLiveData<Boolean>()

        validateUser(service, loginObject,{

            success.value = it?.success == true
        },{
            success.value = false
        })

        return success
    }

    fun getTeams(): LiveData<List<Team>> {
        val teams = MutableLiveData<List<Team>>()

        searchAllTeams(service, {
            Log.e(TAG, "search All Teams Success $it")
            teams.value = it
        },{
            Log.e(TAG, "search All Teams Error $it")
        })

        return teams
    }

    fun createTeam(team: Team): LiveData<ApiResponse> {

        val success = MutableLiveData<ApiResponse>()

        createTeam(service, team, {
            Log.e(TAG, "create Team  $it")
            success.value = it
        }, {
            success.value = ApiResponse("", false, "0")
            Log.e(TAG, "create Event error $it")
        })
        return success
    }

}