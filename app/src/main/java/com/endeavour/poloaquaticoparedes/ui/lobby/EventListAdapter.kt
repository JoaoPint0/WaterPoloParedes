package com.endeavour.poloaquaticoparedes.ui.lobby

import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.OnItemClickListener
import com.endeavour.poloaquaticoparedes.model.Event

class EventListAdapter (val listener : OnItemClickListener): RecyclerView.Adapter<EventListViewHolder>() {

    private var eventList: List<Event> = emptyList()

    fun setEventsList(events: List<Event>) {
        eventList = events
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {

        if (eventList.isNotEmpty()) {
            val event = eventList[position]
            holder.bind(event, listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        return EventListViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}