package com.endeavour.poloaquaticoparedes.ui.event.create

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.endeavour.poloaquaticoparedes.model.Team
import kotlinx.android.synthetic.main.team_view_item.view.*

class TeamSpinnerAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val teams: List<Team>): ArrayAdapter<Team>(context, layoutResource, teams) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view =  LayoutInflater.from(context).inflate(layoutResource, parent, false)
        view.team_name.text = teams[position].name
        return view
    }
}