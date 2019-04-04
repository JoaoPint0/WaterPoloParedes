package com.endeavour.poloaquaticoparedes.ui.lobby

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.OnItemClickListener
import com.endeavour.poloaquaticoparedes.model.Game



class GamesListAdapter (val listener : OnItemClickListener) : RecyclerView.Adapter<GamesListViewHolder>() {

    private var gamesList: List<Game> = emptyList()

    fun isEmpty(): Boolean{
        return gamesList.isEmpty()
    }

    fun setGamesList(games: List<Game>) {
        if(gamesList != games) {
            gamesList = games
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: GamesListViewHolder, position: Int) {

        if (gamesList.isNotEmpty()) {
            val game = gamesList[position]
            holder.bind(game, listener)
        } else {
            holder.bind(null, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesListViewHolder {
        return GamesListViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }
}
