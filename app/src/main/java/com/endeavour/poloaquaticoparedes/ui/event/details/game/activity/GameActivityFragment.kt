package com.endeavour.poloaquaticoparedes.ui.event.details.game.activity

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.details.GameDetailsFragmentArgs
import kotlinx.android.synthetic.main.game_activity_fragment.*

class GameActivityFragment : Fragment() {

    companion object {
        fun newInstance(id: Long): GameActivityFragment {
            val args = Bundle ()
            args.putLong("id", id)
            val fragment = GameActivityFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val adapter = GameActivityAdapter()
    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(GameViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.game_activity_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = arguments?.let {
            val safeArgs = GameDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        activity_list.adapter = adapter
        activity_list.layoutManager = LinearLayoutManager(context)

        viewModel.gameById(id).observe(this, androidx.lifecycle.Observer { game ->

            if(game == null) return@Observer
            adapter.setActivities(game.activity)
        })
    }
}
