package com.endeavour.poloaquaticoparedes.ui.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.Event
import kotlinx.android.synthetic.main.event_card_view_item.view.*
import kotlinx.android.synthetic.main.payments_view_item.view.*

class EventListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(event: Event?, listener: OnItemClickListener) {
        if (event == null) {
            val resources = itemView.resources
            view.payment_status.text = resources.getString(R.string.loading)
        } else {
            showRepoData(event)
            view.setOnClickListener {
                listener.onItemClick(it, event.id)
            }
        }
    }

    private fun showRepoData(event: Event) {

        view.event_name.text = event.name
        view.event_date.text = formatDate(event.date)
        view.event_league.text = getLeagueText(view.context, event.target[0])

        Glide.with(view)
            .load(if (event.picture.isNullOrBlank()) R.drawable.paredes_logo else event.picture)
            .centerCrop()
            .into(view.event_picture)
    }

    companion object {
        fun create(parent: ViewGroup): EventListViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_card_view_item, parent, false)
            return EventListViewHolder(view)
        }
    }
}