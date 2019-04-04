package com.endeavour.poloaquaticoparedes.ui.event.details

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
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.ui.event.EventViewModel
import kotlinx.android.synthetic.main.event_details_fragment.*

class EventDetailsFragment : Fragment() {

    private lateinit var viewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, Injection.provideEventsViewModelFactory(context!!))
            .get(EventViewModel::class.java)

        val id = arguments?.let {
            val safeArgs = EventDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        val sharedPref = context?.getSharedPreferences(
            getString(R.string.shared_preferences), Context.MODE_PRIVATE
        )

        viewModel.getEventById(id).observe(this, Observer {

            if (it != null) {

                Log.e("EventDetails", "$it")

                setupEventPicture(it.picture)

                event_name.text = it.name
                event_location.text = it.location
                event_content.text = it.content
                val formattedDate = formatDate(it.date) + " " + formatTime(it.date)
                event_date.text = formattedDate

                if (sharedPref?.getString(getString(R.string.privileges), "") == "admin") {

                    setupEditEvent(it)
                }
            }
        })
    }

    private fun setupEditEvent(event:Event) {

        edit_event.visibility = View.VISIBLE
        edit_event.setOnClickListener {

            val bundle = Bundle()
            bundle.putLong("id", event.id)
            Navigation.findNavController(it).navigate(R.id.createEventFragment, bundle)
        }
    }

    private fun setupEventPicture(picture: String) {

        loadGlideImage(event_picture, picture)
    }

}
