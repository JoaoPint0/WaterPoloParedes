package com.endeavour.poloaquaticoparedes.ui.event.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.*
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.savvyapps.togglebuttonlayout.Toggle
import kotlinx.android.synthetic.main.create_game_fragment.*
import java.util.*

class CreateGameFragment : Fragment() {

    private var id = 0L
    private val viewModel by lazy {
        ViewModelProviders.of(this, Injection.provideGameViewModelFactory(context!!)).get(
            GameViewModel::class.java
        )
    }
    val teams = mutableListOf<Team>()
    private val adapter by lazy { TeamSpinnerAdapter(context!!, com.endeavour.poloaquaticoparedes.R.layout.team_view_item) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (id != 0L) {

        } else {
            game_date.text = formatDate(Date())
            game_time.text = formatTime(Date())
        }

        viewModel.getTeams().observe(this, androidx.lifecycle.Observer {
            teams.clear()
            teams.addAll(it)
            adapter.setTeams(it)

            game_creation_home_team.adapter = adapter
            game_creation_away_team.adapter = adapter
        })

        game_date.setOnClickListener {

            createDatePicker(fragmentManager!!, it as TextView)
        }

        game_time.setOnClickListener {

            createTimePicker(fragmentManager!!, it as TextView)
        }

        new_team_btn.setOnClickListener {

            CreateTeamDialogFragment().show(fragmentManager!!, "")
        }

        create_game_btn.setOnClickListener {

            val game = createGameObject()

            if (id == 0L) createGame() else editGame()
        }

        game_target_young_leagues_toggle.onToggledListener = { _, selected ->
            if (selected) {
                for (toggle: Toggle in game_target_older_leagues_toggle.toggles) {
                    game_target_older_leagues_toggle.setToggled(toggle.id, false)
                }
            }
        }

        game_target_older_leagues_toggle.onToggledListener = { _, selected ->
            if (selected) {
                for (toggle: Toggle in game_target_young_leagues_toggle.toggles) {
                    game_target_young_leagues_toggle.setToggled(toggle.id, false)
                }
            }
        }
    }

    private fun createGameObject(): Game {

        val (day, month, year) = game_date.text.toString().split("/").map { it.toInt() }
        val (hour, minute) = game_time.text.toString().split(":").map { it.toInt() }

        return Game(
            id,
            getHomeTeam(),
            getAwayTeam(),
            GameParticipants(mutableListOf(),mutableListOf(),mutableListOf(),mutableListOf(),mutableListOf()),
            getGameTargets(),
            Date(year - 1900, month - 1, day, hour, minute, 0),
            game_competition_edit.text.toString(),
            game_location_edit.text.toString(),
            1,
            480000,
            mutableListOf()
        )
    }

    private fun createGame() {

        if (!validateFields()) return

        val game = createGameObject()

        viewModel.createGame(game).observe(this, androidx.lifecycle.Observer {

            if (it.success) {

                val bundle = Bundle()
                bundle.putLong("id", it.id.toLong())
                findNavController(this).navigate(R.id.createdGame, bundle)
            } else {
                Toast.makeText(context, getString(R.string.create_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editGame() {

        if (!validateFields()) return

        val game = createGameObject()

        viewModel.editGame(game).observe(this, androidx.lifecycle.Observer {

            if (it.success) {
                val bundle = Bundle()
                bundle.putLong("id", game.id)
                findNavController(this).navigate(R.id.createdGame, bundle)
            } else {
                Toast.makeText(context, getString(R.string.edit_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteGame(id: Long) {

        viewModel.deleteGame(id).observe(this, androidx.lifecycle.Observer {
            if (it.success == true) {
                findNavController(this).navigate(R.id.mainFragment)
            } else {
                Toast.makeText(context, getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateFields(): Boolean {

        return (game_target_sex_toggle.isSelected(getString(R.string.toggle_error_event_target)) &&
                (game_target_young_leagues_toggle.isSelected(getString(R.string.toggle_error_event_target))
                        || game_target_older_leagues_toggle.isSelected(getString(R.string.toggle_error_event_target))))
    }

    private fun getHomeTeam(): Team {
        return teams[game_creation_home_team.selectedItemPosition]
    }

    private fun getAwayTeam(): Team {
        return teams[game_creation_away_team.selectedItemPosition]
    }

    private fun getGameTargets(): Leagues {

        val toggles = ArrayList<String>()
        toggles.addAll(game_target_young_leagues_toggle.selectedToggles().map { it.title.toString() })
        toggles.addAll(game_target_older_leagues_toggle.selectedToggles().map { it.title.toString() })

        val isMale = game_target_sex_toggle.toggles[0].isSelected

        return when (toggles[0]) {
            resources.getString(R.string.mini_polo) -> Leagues.MINIPOLO
            resources.getString(R.string.sub12) -> Leagues.SUB12
            resources.getString(R.string.infantil) -> if (isMale) Leagues.INFANTIL_MALE else Leagues.INFANTIL_FEMALE
            resources.getString(R.string.juvenil) -> if (isMale) Leagues.JUVENIL_MALE else Leagues.JUVENIL_FEMALE
            resources.getString(R.string.a18) -> if (isMale) Leagues.A18_MALE else Leagues.A18_FEMALE
            resources.getString(R.string.a20) -> if (isMale) Leagues.A20_MALE else Leagues.A20_FEMALE
            resources.getString(R.string.senior) -> if (isMale) Leagues.SENIOR_MALE else Leagues.SENIOR_FEMALE
            else -> return Leagues.SENIOR_MALE
        }
    }
}
