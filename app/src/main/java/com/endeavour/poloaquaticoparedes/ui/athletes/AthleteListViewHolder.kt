package com.endeavour.poloaquaticoparedes.ui.athletes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.getLeagueText
import com.endeavour.poloaquaticoparedes.model.Athlete
import kotlinx.android.synthetic.main.athlete_view_item.view.*

class AthleteListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(athlete: Athlete?) {
        if (athlete == null) {
            val resources = itemView.resources
            view.athlete_name.text = resources.getString(R.string.loading)
        } else {
            showRepoData(athlete)
        }
    }

    private fun showRepoData(athlete: Athlete) {

        view.athlete_name.text = athlete.name
        view.athlete_level.text = getLeagueText(view.context, athlete.level)
        view.athlete_gender.text = when(athlete.gender){
            "M" -> view.context.getString(R.string.male)
            "F" ->  view.context.getString(R.string.female)
            else -> athlete.gender
        }

        view.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardId", athlete.cardId.toString())
            Navigation.findNavController(view).navigate(R.id.profileFragment, bundle)
        }

    }

    companion object {
        fun create(parent: ViewGroup): AthleteListViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.athlete_view_item, parent, false)
            return AthleteListViewHolder(view)
        }
    }
}