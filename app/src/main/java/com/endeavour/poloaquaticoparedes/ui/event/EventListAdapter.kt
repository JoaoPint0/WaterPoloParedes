package com.endeavour.poloaquaticoparedes.ui.event

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.Event

class EventListAdapter: RecyclerView.Adapter<EventListViewHolder>() {

    private var eventList: List<Event> = emptyList()

    fun setEventsList(events: List<Event>) {
        eventList = events
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {

        if (eventList.isNotEmpty()) {
            val payment = eventList[position]
            holder.bind(payment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        return EventListViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}