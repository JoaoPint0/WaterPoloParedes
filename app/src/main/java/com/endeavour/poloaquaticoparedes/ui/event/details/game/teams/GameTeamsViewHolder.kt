package com.endeavour.poloaquaticoparedes.ui.event.details.game.teams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.R
import kotlinx.android.synthetic.main.game_team_view_item.view.*

class GameTeamsViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    fun showData(homeName: String, awayName: String, position: Int, isPlayer: Boolean) {

        view.home_coach_icon.visibility = if (isPlayer) View.GONE else View.VISIBLE
        view.away_coach_icon.visibility = if (isPlayer) View.GONE else View.VISIBLE
        view.home_player_number.apply {
            text = "$position"
            visibility = if (!isPlayer) View.GONE else View.VISIBLE
        }

        view.away_player_number.apply {
            text = "$position"
            visibility = if (!isPlayer) View.GONE else View.VISIBLE
        }
        view.home_player_name.text = homeName
        view.away_player_name.text = awayName
    }

    companion object {
        fun create(parent: ViewGroup): GameTeamsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_team_view_item, parent, false)
            return GameTeamsViewHolder(view)
        }
    }
}