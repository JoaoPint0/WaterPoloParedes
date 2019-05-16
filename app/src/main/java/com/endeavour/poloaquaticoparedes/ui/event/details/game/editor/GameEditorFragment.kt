package com.endeavour.poloaquaticoparedes.ui.event.details.game.editor

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.formatCounter
import com.endeavour.poloaquaticoparedes.model.*
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.create.TeamSpinnerAdapter
import com.endeavour.poloaquaticoparedes.ui.event.details.GameDetailsFragmentArgs
import kotlinx.android.synthetic.main.game_editor_fragment.*


class GameEditorFragment : Fragment() {

    private var round = 1
    private var time = 0L
    private var counter: CountDownTimer? = null
    private lateinit var game: Game

    companion object {
        fun newInstance(id: Long): GameEditorFragment {
            val args = Bundle()
            args.putLong("id", id)
            val fragment = GameEditorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val memberAdapter by lazy { TeamSpinnerAdapter(context!!, com.endeavour.poloaquaticoparedes.R.layout.team_view_item) }
    private val activityAdapter by lazy { TeamSpinnerAdapter(context!!, com.endeavour.poloaquaticoparedes.R.layout.team_view_item) }
    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(GameViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.game_editor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSpinner(activity_type, GameEventType.values().asList())
        setupSpinner(member_type, MemberType.values().asList())

        member_team.adapter = memberAdapter
        activity_team.adapter = activityAdapter

        val id = arguments?.let {
            val safeArgs = GameDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        viewModel.gameById(id).observe(this, androidx.lifecycle.Observer {

            if(it == null) return@Observer

            game = it

            memberAdapter.setTeams(listOf(game.homeTeam, game.awayTeam))
            activityAdapter.setTeams(listOf(game.homeTeam, game.awayTeam))

            if (counter == null) {
                time = game.time
                round = game.round
                setTimerCount()
            }
        })

        member_team.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                member_number.text =
                    if (position == 0) "${game.participants.homeTeamPlayerList.size + 1}" else "${game.participants.awayTeamPlayerList.size + 1}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        member_type.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (MemberType.values()[position] == MemberType.PLAYER) {
                    member_number.visibility = View.VISIBLE
                } else {
                    member_number.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        activity_team.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    setupSpinner(activity_player, game.participants.homeTeamPlayerList)
                } else {
                    setupSpinner(activity_player, game.participants.awayTeamPlayerList)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        activity_type.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (GameEventType.values()[position] == GameEventType.TIME_OUT || GameEventType.values()[position] == GameEventType.MATCH_OVER) {
                    activity_player.visibility = View.GONE
                } else {
                    activity_player.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        next_round.setOnClickListener {
            round++
            setTimerCount()
        }

        play_button.setOnClickListener {
            if (counter != null) return@setOnClickListener
            counter = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                }

                override fun onTick(millisUntilFinished: Long) {
                    time = millisUntilFinished
                    setTimerCount()
                }
            }.start()
        }
        pause_button.setOnClickListener {
            cancelCounter()
        }
        reset_button.setOnClickListener {
            cancelCounter()
            time = 0
            setTimerCount()
        }

        add_min.setOnClickListener {
            cancelCounter()
            time += 60000
            setTimerCount()
        }
        subtract_min.setOnClickListener {
            cancelCounter()
            if (time >= 60000) time -= 60000 else time = 0
            setTimerCount()
        }

        create_member_btn.setOnClickListener {
            val memberType = MemberType.values()[member_type.selectedItemPosition]
            val name = member_name_edit.text.toString()

            when (memberType) {
                MemberType.PLAYER -> {
                    if (member_team.selectedItemPosition == 0) {
                        addPlayer(game.participants.homeTeamPlayerList, name, game.homeTeam.name)
                    } else {
                        addPlayer(game.participants.awayTeamPlayerList, name, game.awayTeam.name)
                    }
                }
                MemberType.COACH -> {
                    if (member_team.selectedItemPosition == 0) {
                        addCoach(game.participants.homeTeamCoachList, name, game.homeTeam.name)
                    } else {
                        addCoach(game.participants.awayTeamCoachList, name, game.awayTeam.name)
                    }
                }
                MemberType.REFEREE -> {
                    game.participants.refereeList.add(GamePerson(0L, name, 0, MemberType.REFEREE, ""))
                }
            }

            viewModel.editGame(game).observe(this, Observer {
                if (it.success) {
                    member_name_edit.text?.clear()
                    member_number.text = if (member_team.selectedItemPosition == 0) "${game.participants.homeTeamPlayerList.size + 1}" else "${game.participants.awayTeamPlayerList.size + 1}"
                }
            })
        }

        create_activity_btn.setOnClickListener {

            val gameType = GameEventType.values()[activity_type.selectedItemPosition]
            game.activity.add(GameEvent(gameType, activity_team.selectedItemPosition, activity_player.selectedItem as String, round, time))
            viewModel.editGame(game)
        }
    }

    override fun onStart() {
        if (::game.isInitialized) {
            memberAdapter.setTeams(listOf(game.homeTeam, game.awayTeam))
            activityAdapter.setTeams(listOf(game.homeTeam, game.awayTeam))
        }
        super.onStart()
    }

    private fun addPlayer(players: MutableList<GamePerson>, name: String, team: String) {
        players.add(GamePerson(0L, name, players.size + 1, MemberType.PLAYER, team))
    }

    private fun addCoach(coaches: MutableList<GamePerson>, name: String, team: String) {
        coaches.add(GamePerson(0L, name, 0, MemberType.COACH, team))
    }

    private fun setTimerCount() {
        timer_count.text = formatCounter(time)
        round_count.text = round.toString()

        if (::game.isInitialized) {
            game.time = time
            game.round = round
            viewModel.editGame(game)
        }
    }

    private fun cancelCounter() {
        if (counter != null) {
            counter!!.cancel()
            counter = null
        }
    }

    private fun setupSpinner(spinner: AppCompatSpinner, list: List<Any>) {
        GameEventSpinnerAdapter(context!!, R.layout.team_view_item, list)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
    }

    override fun onStop() {
        counter?.cancel()
        counter = null
        super.onStop()
    }
}
