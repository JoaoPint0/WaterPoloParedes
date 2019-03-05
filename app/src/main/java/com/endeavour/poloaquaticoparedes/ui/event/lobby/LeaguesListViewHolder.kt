package com.endeavour.poloaquaticoparedes.ui.event.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.Leagues
import kotlinx.android.synthetic.main.league_view_item.view.*

class LeaguesListViewHolder (val view: View) : RecyclerView.ViewHolder(view)  {

    fun bind(leagues: Leagues?) {
        if (leagues == null) {
            val resources = itemView.resources
            view.league_type.text = resources.getString(R.string.loading)
        } else {
            showRepoData(leagues)
        }
    }

    private fun showRepoData(league: Leagues) {
        val resources = itemView.resources
        view.setOnClickListener {
            val bundle =  Bundle()
            bundle.putSerializable("league", league)
            Navigation.findNavController(it).navigate(R.id.eventsFragment, bundle)
        }
        when(league){
            Leagues.SENIOR_MALE -> {
                view.league_type.text = resources.getString(R.string.senior)
                view.league_sex.text = resources.getString(R.string.male)
            }
            Leagues.SENIOR_FEMALE -> {
                view.league_type.text = resources.getString(R.string.senior)
                view.league_sex.text = resources.getString(R.string.female)
            }
            Leagues.A20_MALE -> {
                view.league_type.text = resources.getString(R.string.a20)
                view.league_sex.text = resources.getString(R.string.male)
            }
            Leagues.A20_FEMALE -> {
                view.league_type.text = resources.getString(R.string.a20)
                view.league_sex.text = resources.getString(R.string.female)
            }
            Leagues.A18_MALE -> {
                view.league_type.text = resources.getString(R.string.a18)
                view.league_sex.text = resources.getString(R.string.male)
            }
            Leagues.A18_FEMALE -> {
                view.league_type.text = resources.getString(R.string.a18)
                view.league_sex.text = resources.getString(R.string.female)
            }
            Leagues.JUVENIL_MALE -> {
                view.league_type.text = resources.getString(R.string.juvenil)
                view.league_sex.text = resources.getString(R.string.male)
            }
            Leagues.JUVENIL_FEMALE -> {
                view.league_type.text = resources.getString(R.string.juvenil)
                view.league_sex.text = resources.getString(R.string.female)
            }
            Leagues.INFANTIL_MALE -> {
                view.league_type.text = resources.getString(R.string.infantil)
                view.league_sex.text = resources.getString(R.string.male)
            }
            Leagues.INFANTIL_FEMALE -> {
                view.league_type.text = resources.getString(R.string.infantil)
                view.league_sex.text = resources.getString(R.string.female)
            }
            Leagues.SUB12 ->{
                view.league_type.text = resources.getString(R.string.sub12)
                view.league_sex.text = ""
            }
            Leagues.MINIPOLO -> {
                view.league_type.text = resources.getString(R.string.mini_polo)
                view.league_sex.text = ""
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): LeaguesListViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.league_view_item, parent, false)
            return LeaguesListViewHolder(view)
        }
    }
}