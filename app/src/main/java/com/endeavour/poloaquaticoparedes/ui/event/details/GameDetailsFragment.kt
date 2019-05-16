package com.endeavour.poloaquaticoparedes.ui.event.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.databinding.GameDetailsFragmentBinding
import com.endeavour.poloaquaticoparedes.model.Game
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.details.game.GamePagerAdapter
import com.endeavour.poloaquaticoparedes.ui.event.details.game.activity.GameActivityFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.editor.GameEditorFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.info.GameInfoFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.teams.GameTeamsFragment
import kotlinx.android.synthetic.main.game_details_fragment.*


class GameDetailsFragment : Fragment() {

    private var pagerAdapter: FragmentPagerAdapter? = null
    private lateinit var binding: GameDetailsFragmentBinding
    private val sharedPref by lazy { context!!.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) }
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!, Injection.provideGameViewModelFactory(context!!)).get(
            GameViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameDetailsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = arguments?.let {
            val safeArgs = GameDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        viewModel.gameById(id).observe(this, Observer {game ->

            if(game != null){
                binding.game = game

                if (pagerAdapter == null) {
                    val position = when {
                        sharedPref.getString(getString(R.string.privileges), "") == "admin" -> 3
                        game.activity.isNotEmpty() -> 0
                        else -> 2
                    }
                    setUpViewPager(game, position)
                }
            }
        })

    }

    private fun setUpViewPager(game: Game, position: Int) {

        val isAdmin = false//sharedPref.getString(getString(R.string.privileges), "") == "admin"

        val list = mutableListOf<Fragment>()
        val names = mutableListOf<String>()

        if(game.activity.isNotEmpty()) {
            list.add(GameActivityFragment.newInstance(game.id))
            list.add(GameTeamsFragment.newInstance(game.id))

            names.apply {
                add("Live")
                add("Equipas")
            }
        }

        list.add(GameInfoFragment.newInstance(game.id))
        names.add("Info")

        if (isAdmin){
            list.add(GameEditorFragment.newInstance(game.id))
            names.add("Admin")
        }

        if(list.size == 1) tabs.visibility = View.GONE else View.VISIBLE

        pagerAdapter = GamePagerAdapter(childFragmentManager, list, names)

        game_pager.apply {
            offscreenPageLimit = 1
            adapter = pagerAdapter
            setCurrentItem(position, false)
        }
        tabs.setupWithViewPager(game_pager)
    }
}
