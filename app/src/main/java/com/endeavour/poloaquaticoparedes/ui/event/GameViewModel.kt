package com.endeavour.poloaquaticoparedes.ui.event

import androidx.lifecycle.*
import com.endeavour.poloaquaticoparedes.model.ApiResponse
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.Team
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class GameViewModel(private val repository: WaterPoloRepository) : ViewModel() {

    private var count = 0L
    private val version = MutableLiveData<Long>()

    private val dbEvents = Transformations.switchMap(version){ repository.searchAllEvents("")}
    val dbGames =  Transformations.switchMap(version){ repository.searchAllGames()}

    val events = MediatorLiveData<List<Event>>()
    val games = MediatorLiveData<List<Game>>()

    private var currentFilters = emptySet<String>()

    init {
        events.addSource(dbEvents) { result: List<Event>? ->
            result?.let { events.value = sortEvents(it, currentFilters)}
        }
        games.addSource(dbGames) { result: List<Game>? ->
            result?.let { games.value = sortGames(it, currentFilters)}
        }
    }

    fun rearrangeLobby(filters: Set<String>) {
        version.value = count++
        dbGames.value?.let {
            games.value = sortGames(it, filters)
        }
        dbEvents.value?.let {
            events.value = sortEvents(it, filters)
        }.also { currentFilters = filters }
    }

    private fun sortEvents(eventsList: List<Event>, currentFilters: Set<String>) = eventsList.filter { currentFilters.contains(it.target[0]?.name)}
    private fun sortGames(gamesList: List<Game>, currentFilters: Set<String>) = gamesList.filter { currentFilters.contains(it.target?.name)}

    fun getTeams() = repository.getTeams()
    fun createTeam(team: Team)= repository.createTeam(team)

    fun gameById(id:Long) = repository.gameById(id)

    fun createGame(game: Game) =  repository.createGame(game)
    fun editGame(game: Game) = repository.editGame(game)
    fun deleteGame(id: Long) =  repository.deleteGame(id)

}