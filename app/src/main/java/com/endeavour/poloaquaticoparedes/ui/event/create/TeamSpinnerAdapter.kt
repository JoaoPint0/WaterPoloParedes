package com.endeavour.poloaquaticoparedes.ui.event.create

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.endeavour.poloaquaticoparedes.loadGlideImage
import com.endeavour.poloaquaticoparedes.model.Team
import kotlinx.android.synthetic.main.team_view_item.view.*

class TeamSpinnerAdapter(context: Context, @LayoutRes private val layoutResource: Int): ArrayAdapter<Team>(context, layoutResource) {

    private var teams: MutableList<Team> = mutableListOf()

    fun setTeams(list: List<Team>){

        teams.clear()
        teams.addAll(list)
        clear()
        addAll(teams)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view =  LayoutInflater.from(context).inflate(layoutResource, parent, false)
        view.team_name.text = teams[position].name
        loadGlideImage(view.team_logo, teams[position].logo)
        return view
    }
}