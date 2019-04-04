package com.endeavour.poloaquaticoparedes.ui.event.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.databinding.GameDetailsFragmentStartBinding
import com.endeavour.poloaquaticoparedes.model.GameEventType
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.details.game.GamePagerAdapter
import com.endeavour.poloaquaticoparedes.ui.event.details.game.activity.GameActivityFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.editor.GameEditorFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.info.GameInfoFragment
import com.endeavour.poloaquaticoparedes.ui.event.details.game.teams.GameTeamsFragment
import kotlinx.android.synthetic.main.game_details_fragment_start.*


class GameDetailsFragment : Fragment() {

    private var pagerAdapter: FragmentPagerAdapter? = null
    private lateinit var binding: GameDetailsFragmentStartBinding
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
        binding = GameDetailsFragmentStartBinding.inflate(inflater)
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
        } ?: "0"

        viewModel.getGameById(id).observe(this, androidx.lifecycle.Observer {
            binding.game = it
            it.id = id

            if (pagerAdapter == null) {
                val position = when {
                    sharedPref.getString(getString(R.string.privileges), "") == "admin" -> 3
                    it.activity.isNotEmpty() -> 0
                    else -> 2
                }
                setUpViewPager(id, position)
            }

            if(it.activity.filter { it.type == GameEventType.MATCH_OVER }.any()){
                game_details_timer.visibility = View.GONE
                game_details_round.visibility = View.GONE
            }
        })
    }

    private fun setUpViewPager(id: String, position: Int) {

        val isAdmin = sharedPref.getString(getString(R.string.privileges), "") == "admin"

        val list = mutableListOf(
            GameActivityFragment.newInstance(id),
            GameTeamsFragment.newInstance(id),
            GameInfoFragment.newInstance(id)
        )

        if (isAdmin) list.add(GameEditorFragment.newInstance(id))

        pagerAdapter = GamePagerAdapter(childFragmentManager, list)

        game_pager.apply {
            offscreenPageLimit = 1
            adapter = pagerAdapter
            setCurrentItem(position, false)
        }
        tabs.setupWithViewPager(game_pager)
    }
}
