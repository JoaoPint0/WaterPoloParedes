package com.endeavour.poloaquaticoparedes.ui.event.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.databinding.CreateEventFragmentBinding
import com.endeavour.poloaquaticoparedes.model.Event
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.endeavour.poloaquaticoparedes.ui.event.EventViewModel
import kotlinx.android.synthetic.main.create_event_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class CreateEventFragment : Fragment() {

    private lateinit var viewModel: EventViewModel
    private lateinit var binding: CreateEventFragmentBinding

    private var id = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = CreateEventFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, Injection.provideEventsViewModelFactory(context!!))
            .get(EventViewModel::class.java)
        binding.viewmodel = viewModel

        id = arguments?.let {
            val safeArgs = CreateEventFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        if (id > 0L) {

            setupDeleteEvent()
            setupEditEvent()
        }else{

            event_date.text = formatDate(Date())
            event_time.text = formatTime(Date())
        }

        setupPrioritySpinner()

        event_date.setOnClickListener {

            createDatePicker(fragmentManager!!, it as TextView)
        }

        event_time.setOnClickListener {

            createTimePicker(fragmentManager!!, it as TextView)
        }

        create_event_btn.setOnClickListener {

            if(id == 0L) createEvent(createEventObject()) else editEvent(createEventObject())
        }
    }

    private fun setupEditEvent() {
        create_event_btn.text = getString(R.string.edit)

        viewModel.getEventById(id).observe(this,androidx.lifecycle.Observer {

            event_name_edit.text = it.name.toEditable()
            event_location_edit.text = it.location.toEditable()

            event_date.text = formatDate(it.date)
            event_time.text = formatTime(it.date)

            for (target in it.target){
                when (target) {
                    Leagues.MINIPOLO-> event_target_young_leagues_toggle.setToggled(R.id.toggle_mini_polo, true)
                    Leagues.SUB12 -> event_target_young_leagues_toggle.setToggled(R.id.toggle_sub12, true)
                    Leagues.INFANTIL_MALE -> {
                        event_target_young_leagues_toggle.setToggled(R.id.toggle_infantil, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_male, true)
                    }
                    Leagues.INFANTIL_FEMALE -> {
                        event_target_young_leagues_toggle.setToggled(R.id.toggle_infantil, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_female, true)
                    }
                    Leagues.JUVENIL_MALE -> {
                        event_target_young_leagues_toggle.setToggled(R.id.toggle_juvenil, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_male, true)
                    }
                    Leagues.JUVENIL_FEMALE -> {
                        event_target_young_leagues_toggle.setToggled(R.id.toggle_juvenil, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_female, true)
                    }
                    Leagues.A18_MALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_male, true)
                    }
                    Leagues.A18_FEMALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_female, true)
                    }
                    Leagues.A20_MALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_male, true)
                    }
                    Leagues.A20_FEMALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_female, true)
                    }
                    Leagues.SENIOR_MALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_male, true)
                    }
                    Leagues.SENIOR_FEMALE-> {
                        event_target_older_leagues_toggle.setToggled(R.id.toggle_senior, true)
                        event_target_sex_toggle.setToggled(R.id.toggle_female, true)
                    }
                }
            }

            event_priority.setSelection(it.priority.toInt())

            event_image_edit.text = it.picture.toEditable()
            event_content_edit.text = it.content.toEditable()

        })
    }

    private fun setupDeleteEvent() {
        delete_event_btn.visibility = View.VISIBLE
        delete_event_btn.setOnClickListener {
            deleteEvent(id)
        }
    }

    private fun setupPrioritySpinner() {
        ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_item,
                listOf(getString(R.string.low_priority), getString(R.string.high_priority))
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            event_priority.adapter = adapter
        }
    }

    private fun validateFields(): Boolean {

        val isGame = viewModel.isGameEvent.get()

        return  (!isGame && event_name_edit.isNotEmpty() &&
                event_image_edit.isNotEmpty() &&
                (!isGame && event_content_edit.isNotEmpty() || isGame) &&
                event_target_sex_toggle.isSelected(getString(R.string.toggle_error_event_target)) &&
                (event_target_young_leagues_toggle.isSelected(getString(R.string.toggle_error_event_target))
                        || event_target_older_leagues_toggle.isSelected(getString(R.string.toggle_error_event_target))))
    }

    private fun createEventObject(): Event {

        val (day, month, year) = event_date.text.toString().split("/").map { it.toInt() }
        val (hour, minute) = event_time.text.toString().split(":").map { it.toInt() }
        val targets = getEventTargets(event_target_young_leagues_toggle,event_target_older_leagues_toggle,event_target_sex_toggle)

        return Event(id,
            isPublicEvent.isChecked,
            event_name_edit.text.toString(),
            event_image_edit.text.toString(),
            event_location_edit.text.toString(),
            Date(year-1900, month-1, day, hour, minute, 0),
            getEventPriority(),
            targets,
            event_content_edit.text.toString())
    }

    private fun createEvent(event: Event) {

        if (!validateFields()) return

        viewModel.createEvent(event).observe(this, androidx.lifecycle.Observer {

            if(it.success){

                val bundle =  Bundle()
                bundle.putLong("id",it.id.toLong())
                findNavController(this).navigate(R.id.createdEvent, bundle)
            } else {
                Toast.makeText(context, getString(R.string.create_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editEvent(event: Event) {

        if (!validateFields()) return

        viewModel.editEvent(event).observe(this,androidx.lifecycle.Observer {

            if(it == true){
                val bundle =  Bundle()
                bundle.putLong("id",event.id)
                findNavController(this).navigate(R.id.createdEvent, bundle)
            } else {
                Toast.makeText(context, getString(R.string.edit_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteEvent(id: Long) {

        viewModel.deleteEvent(id).observe(this, androidx.lifecycle.Observer {
            if(it == true){
                findNavController(this).navigate(R.id.mainFragment)
            } else {
                Toast.makeText(context, getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getEventPriority(): Long {
        Log.e("CreateEvent", "${event_priority.selectedItemId}")
        return event_priority.selectedItemId
    }
}
