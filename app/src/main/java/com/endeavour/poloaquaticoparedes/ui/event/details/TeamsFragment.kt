package com.endeavour.poloaquaticoparedes.ui.event.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.ui.event.EventViewModel

class TeamsFragment : Fragment() {

    companion object {
        fun newInstance() = TeamsFragment()
    }

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, Injection.provideEventsViewModelFactory(context!!))
            .get(EventViewModel::class.java)

        val id = arguments?.let {
            val safeArgs = EventDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        viewModel.getEventById(id).observe(this, Observer {

            if( it != null){


            }
        })
    }

}
