package com.endeavour.poloaquaticoparedes.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.formatDate
import com.endeavour.poloaquaticoparedes.model.Event
import kotlinx.android.synthetic.main.event_card_view_item.view.*
import kotlinx.android.synthetic.main.payments_view_item.view.*

class EventListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(event: Event?) {
        if (event == null) {
            val resources = itemView.resources
            view.payment_status.text = resources.getString(R.string.loading)
        } else {
            showRepoData(event)
        }
    }

    private fun showRepoData(event: Event) {

        view.event_name.text = event.name
        if (event.date != null) view.event_date.text = formatDate(event.date)

        Glide.with(view.context)
                .load(event.picture)
                .apply(RequestOptions().centerCrop())
                .into(view.event_picture)

        view.setOnClickListener {
            val bundle =  Bundle()
            bundle.putLong("id",event.id)
            findNavController(it).navigate(R.id.eventDetailsFragment, bundle)
        }
    }

    companion object {
        fun create(parent: ViewGroup): EventListViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_card_view_item, parent, false)
            return EventListViewHolder(view)
        }
    }
}