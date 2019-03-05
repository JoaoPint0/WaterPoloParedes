package com.endeavour.poloaquaticoparedes.ui.event

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.ui.athletes.AthleteListAdapter
import kotlinx.android.synthetic.main.athletes_fragment.*
import kotlinx.android.synthetic.main.events_fragment.*

class EventFragment : Fragment() {

    private lateinit var viewModel: EventViewModel

    private val adapter = EventListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.events_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, Injection.provideEventsViewModelFactory(context!!))
                .get(EventViewModel::class.java)

        setupSearchCardClick()

        val sharedPref = context?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        if (sharedPref?.getString(getString(R.string.privileges), "") == "admin") {

            create_event.visibility = View.VISIBLE
            create_event.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("id", 0L)
                Navigation.findNavController(it).navigate(R.id.createEventFragment, bundle)
            }

            create_game.visibility = View.VISIBLE
            create_game.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("id", 0L)
                Navigation.findNavController(it).navigate(R.id.createGameFragment, bundle)
            }
        }

        events_list.adapter = adapter
        events_list.layoutManager = LinearLayoutManager(context)

        val league = arguments?.getSerializable("league")

        viewModel.events.observe(this, Observer { events ->

            adapter.setEventsList(events.sortedByDescending { it.date })
            showErrorLoadingPayments(events.isEmpty())
        })

        setupAthleteSearch()
        viewModel.searchEvent("")
    }

    private fun setupAthleteSearch() {
        searchViewEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                viewModel.searchEvent(newText)
                return false
            }

            override fun onQueryTextSubmit(search: String): Boolean {

                viewModel.searchEvent(search)
                return false
            }
        })
    }

    private fun setupSearchCardClick() {
        searchCardEvents.setOnClickListener {

            searchViewEvents.isIconified = false
            searchViewEvents.requestFocus()
        }
    }

    private fun showErrorLoadingPayments(isEmpty: Boolean) {

        if (isEmpty) {
            events_list.visibility = View.GONE
            loading_events.visibility = View.VISIBLE
            loading_events.text = activity?.getString(R.string.empty_list) ?: ""
        } else {
            events_list.visibility = View.VISIBLE
            loading_events.visibility = View.GONE
        }
    }


}
