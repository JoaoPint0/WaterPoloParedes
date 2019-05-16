package com.endeavour.poloaquaticoparedes.ui.event.details.game.teams

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.endeavour.poloaquaticoparedes.Injection

import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.model.GamePerson
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.details.GameDetailsFragmentArgs
import kotlinx.android.synthetic.main.game_teams_fragment.*

class GameTeamsFragment : Fragment() {

    private val playersAdapter = GameTeamsAdapter()
    private val coachAdapter = GameTeamsAdapter()

    companion object {
        fun newInstance(id: Long): GameTeamsFragment {
            val args = Bundle ()
            args.putLong("id", id)
            val fragment = GameTeamsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(GameViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_teams_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = arguments?.let {
            val safeArgs = GameDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        players_list.apply {
            adapter = playersAdapter
            layoutManager = LinearLayoutManager(context)
        }
        coaches_list.apply {
            adapter = coachAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.gameById(id).observe(this, androidx.lifecycle.Observer { game ->
            if(game == null) return@Observer
            home_team_title.text = game.homeTeam.name
            away_team_title.text = game.awayTeam.name

            playersAdapter.setTeams(game.participants, true)
            coachAdapter.setTeams(game.participants, false)

            referees_names.text = getRefereeNames(game)
        })
    }

    private fun getRefereeNames(game: Game): String {
        var result = ""

        for (referee: GamePerson in game.participants.refereeList){
            result += " ${referee.name},"
        }

        return result
    }

}
