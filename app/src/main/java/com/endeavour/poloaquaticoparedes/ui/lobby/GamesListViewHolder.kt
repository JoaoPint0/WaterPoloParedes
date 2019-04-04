package com.endeavour.poloaquaticoparedes.ui.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.Game
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import kotlinx.android.synthetic.main.game_view_item.view.*

class GamesListViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {

    fun bind(game: Game?, listener: OnItemClickListener?) {
        if (game == null) {
            val resources = itemView.resources
            view.game_score_or_time.text = view.context.getString(R.string.no_games_found)
        } else {
            showRepoData(game)
            view.setOnClickListener {
                listener?.onItemClick(view, game.id)
            }
        }
    }

    private fun showRepoData(game: Game) {

        loadGlideImage(view.home_team_logo, game.home.logo)
        view.home_team_name.text = game.home.name

        loadGlideImage(view.away_team_logo, game.away.logo)
        view.away_team_name.text = game.away.name

        view.game_date.text = formatGameDate(game.date)
        view.game_competition.text = game.competition
        view.game_score_or_time.text = getGameScore(game, true)
        view.game_league.text = getLeagueText(view.context, game.target)
    }

    companion object {
        fun create(parent: ViewGroup): GamesListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_view_item, parent, false)

            view.layoutParams.width = parent.measuredWidth * 9 / 10

            return GamesListViewHolder(view)
        }
    }
}