package com.endeavour.poloaquaticoparedes.ui.event.details.game.teams

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.GameParticipants
import com.endeavour.poloaquaticoparedes.model.GamePerson
import com.endeavour.poloaquaticoparedes.model.Team

class GameTeamsAdapter : RecyclerView.Adapter<GameTeamsViewHolder>() {

    private var participants = GameParticipants(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

    private var isPlayerList = true

    fun setTeams(participants: GameParticipants, isPlayerList: Boolean) {
        this.participants = participants
        this.isPlayerList = isPlayerList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GameTeamsViewHolder, position: Int) {

        holder.showData(
            getName(if(isPlayerList) participants.homeTeamPlayerList else participants.homeTeamCoachList, position),
            getName(if(isPlayerList) participants.awayTeamPlayerList else participants.awayTeamCoachList, position),
            position + 1,
            isPlayerList
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameTeamsViewHolder {
        return GameTeamsViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return if (isPlayerList){
            biggestSize(participants.homeTeamPlayerList, participants.awayTeamPlayerList)
        } else {
            biggestSize(participants.homeTeamCoachList, participants.awayTeamCoachList)
        }
    }

    private fun biggestSize(l1 : List<Any>, l2:List<Any>): Int{
        return if(l1.size > l2.size) l1.size else l2.size
    }

    private fun getName(list: List<GamePerson>, position: Int): String{

        return if(list.size > position){
            list[position].name
        } else {
            ""
        }
    }
}