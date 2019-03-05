package com.endeavour.poloaquaticoparedes.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.endeavour.poloaquaticoparedes.model.ApiResponse
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.Team
import com.endeavour.poloaquaticoparedes.repository.WaterPoloRepository

class GameViewModel(private val repository: WaterPoloRepository) : ViewModel() {

    fun getTeams():LiveData<List<Team>>{
        return repository.getTeams()
    }

    fun createTeam(team: Team): LiveData<ApiResponse> {

        return repository.createTeam(team)
    }

    fun createGame(game: Game): LiveData<ApiResponse> {

        return repository.createGame(game)
    }

    fun editGame(game: Game): LiveData<Boolean> {

        return repository.editGame(game)
    }

    fun deleteGame(id: Long): LiveData<Boolean> {

        return repository.deleteGame(id)
    }

}