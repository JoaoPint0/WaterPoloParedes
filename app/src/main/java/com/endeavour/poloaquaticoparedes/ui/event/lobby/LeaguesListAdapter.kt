package com.endeavour.poloaquaticoparedes.ui.event.lobby

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.Leagues

class LeaguesListAdapter : RecyclerView.Adapter<LeaguesListViewHolder>() {

    override fun onBindViewHolder(holder: LeaguesListViewHolder, position: Int) {


        val payment = Leagues.values()[position]
        holder.bind(payment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaguesListViewHolder {
        return LeaguesListViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return Leagues.values().size
    }

}
