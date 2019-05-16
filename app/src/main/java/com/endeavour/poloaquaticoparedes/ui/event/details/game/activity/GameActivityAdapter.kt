package com.endeavour.poloaquaticoparedes.ui.event.details.game.activity

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.GameEvent

class GameActivityAdapter : RecyclerView.Adapter<GameActivityViewHolder>() {

    private var gameEvents = emptyList<GameEvent>()

    fun setActivities(list: MutableList<GameEvent>){
        val periodComparator = compareBy<GameEvent> {it.period}
        val periodAndTimeComparator = periodComparator.thenByDescending {it.time}
        list.sortWith(periodAndTimeComparator)
        list.reverse()

        if(gameEvents != list) {
            gameEvents = list
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GameActivityViewHolder.create(parent)

    override fun getItemCount() = gameEvents.size

    override fun onBindViewHolder(holder: GameActivityViewHolder, position: Int) {
        if(gameEvents.isEmpty()) return
        holder.showData(gameEvents[position], position == gameEvents.size-1 || gameEvents[position].period != gameEvents[position +1].period)
    }

}
