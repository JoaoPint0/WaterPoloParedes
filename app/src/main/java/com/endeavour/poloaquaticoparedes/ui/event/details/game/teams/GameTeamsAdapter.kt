package com.endeavour.poloaquaticoparedes.ui.event.details.game.teams

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.Team

class GameTeamsAdapter : RecyclerView.Adapter<GameTeamsViewHolder>() {

    private var awayTeam = Team()
    private var homeTeam = Team()

    private var isPlayerList = true

    fun setTeams(home: Team, away: Team, isPlayerList: Boolean) {
        homeTeam = home
        awayTeam = away
        this.isPlayerList = isPlayerList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GameTeamsViewHolder, position: Int) {

        holder.showData(
            getName(homeTeam, position),
            getName(awayTeam, position),
            position + 1,
            isPlayerList
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameTeamsViewHolder {
        return GameTeamsViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return if (isPlayerList) biggestSize(awayTeam.players, homeTeam.players) else biggestSize(awayTeam.coaches, homeTeam.coaches)
    }

    private fun biggestSize(l1 : List<Any>, l2:List<Any>): Int{
        return if(l1.size > l2.size) l1.size else l2.size
    }

    private fun getName(team: Team, position: Int): String{

        return if(isPlayerList && team.players.size > position){
            team.players[position]
        } else if(team.coaches.size > position){
            team.coaches[position]
        } else {
            ""
        }
    }
}