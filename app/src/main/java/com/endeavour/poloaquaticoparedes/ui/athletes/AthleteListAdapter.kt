package com.endeavour.poloaquaticoparedes.ui.athletes

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.Athlete

class AthleteListAdapter : RecyclerView.Adapter<AthleteListViewHolder>() {

    private var athletesList: List<Athlete> = emptyList()

    fun setAthletesList(payments: List<Athlete>) {
        athletesList = payments
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AthleteListViewHolder, position: Int) {

        if (athletesList.isNotEmpty()) {
            val payment = athletesList[position]
            holder.bind(payment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteListViewHolder {
        return AthleteListViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return athletesList.size
    }
}