package com.endeavour.poloaquaticoparedes.ui.lobby

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.databinding.LobbyFragmentBinding
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import kotlinx.android.synthetic.main.lobby_fragment.*
import java.util.*
import androidx.recyclerview.widget.PagerSnapHelper
import com.endeavour.poloaquaticoparedes.OnItemClickListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd

class LobbyFragment : Fragment() {

    private val eventAdapter = EventListAdapter(object : OnItemClickListener{
        override fun onItemClick(view: View, id: String) {
            loadFullScreenAdd()
            val bundle =  Bundle()
            bundle.putLong("id",id.toLong())
            findNavController(view).navigate(R.id.eventDetailsFragment, bundle)
        }
    })
    private val gamesAdapter = GamesListAdapter(object : OnItemClickListener{
        override fun onItemClick(view: View, id: String) {
            loadFullScreenAdd()
            val bundle =  Bundle()
            bundle.putString("id",id)
            findNavController(view).navigate(R.id.viewGame, bundle, null, null)
        }
    })
    private lateinit var binding: LobbyFragmentBinding
    private val sharedPref by lazy { context!!.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) }
    private val viewModel by lazy {
        ViewModelProviders.of(this, Injection.provideGameViewModelFactory(context!!)).get(
            GameViewModel::class.java
        )
    }

    private val interstitialAd by lazy {  PublisherInterstitialAd(activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LobbyFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.admin = sharedPref.getString(getString(R.string.privileges), "") == "admin"

        if(sharedPref.getString(getString(R.string.privileges), "") == "public") {
            val adRequest = PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()

            ad_view.apply {
                recordManualImpression()
                loadAd(adRequest)
                visibility = View.VISIBLE
            }

            if(interstitialAd.adUnitId.isNullOrBlank()) {
                interstitialAd.apply {
                    adUnitId = "ca-app-pub-9186906227587004/4676959802"
                    loadAd(PublisherAdRequest.Builder().build())
                }
            }
        }

        viewModel.rearrangeLobby(sharedPref.getStringSet("filter_leagues", Leagues.values().map { it.name }.toSet()) ?: emptySet())

        create_event.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("id", 0L)
            Navigation.findNavController(it).navigate(R.id.createEventFragment, bundle)
        }

        create_game.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "0")
            Navigation.findNavController(it).navigate(com.endeavour.poloaquaticoparedes.R.id.createGameFragment, bundle)
        }

        events_filter.setOnClickListener {
            val bottomDialog = LeaguesListDialogFragment.newInstance()
            bottomDialog.show(fragmentManager!!, "leagues")
            bottomDialog.setViewModel(viewModel)
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        games_list.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(games_list)
        games_list.adapter = gamesAdapter

        viewModel.games.observe(this, androidx.lifecycle.Observer { games ->

            Log.e("LobbyFragment", "games $games")
            val wasEmpty = gamesAdapter.isEmpty()
            gamesAdapter.setGamesList(games)
            binding.gamesExist = !games.isNullOrEmpty()

            if (wasEmpty) {
                val index = games.map { it.date.time - Date().time }.indexOfFirst { it > 0 }
                layoutManager.scrollToPositionWithOffset(if (index == -1) games.size - 1 else index, games_list.measuredWidth * 1 / 12)
            }
        })

        viewModel.events.observe(this, Observer { events ->
            val filtered = if (sharedPref.getString(getString(R.string.privileges), "") == "public") events.filter { it.publicEvent } else events

            eventAdapter.setEventsList(filtered.sortedByDescending { it.date })
            binding.eventsExist = !filtered.isNullOrEmpty()
        })

        events_list.adapter = eventAdapter
        events_list.layoutManager = LinearLayoutManager(context)

    }

    override fun onResume() {
        viewModel.rearrangeLobby(sharedPref.getStringSet("filter_leagues", Leagues.values().map { it.name }.toSet()) ?: emptySet())
        super.onResume()
    }

    private fun loadFullScreenAdd(){
        if(sharedPref.getString(getString(R.string.privileges), "") == "public" && interstitialAd.isLoaded &&  showFullScreenAdd()) {
            interstitialAd.show()
            interstitialAd.adListener = object : AdListener() {
                override fun onAdClosed() {
                    interstitialAd.loadAd(PublisherAdRequest.Builder().build())
                    sharedPref.edit().putLong("last_add", Date().time).apply()
                }
            }
        }
    }

    private fun showFullScreenAdd(): Boolean {
        val time = sharedPref.getLong("last_add", 0L)
        return Date().time  > time + 300000
    }

}
