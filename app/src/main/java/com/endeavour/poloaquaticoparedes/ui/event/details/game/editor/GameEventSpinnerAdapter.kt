package com.endeavour.poloaquaticoparedes.ui.event.details.game.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.GameEventType
import com.endeavour.poloaquaticoparedes.model.MemberType
import kotlinx.android.synthetic.main.team_view_item.view.*

class GameEventSpinnerAdapter (context: Context, @LayoutRes private val layoutResource: Int, private val list: List<Any>): ArrayAdapter<Any>(context, layoutResource, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view =  LayoutInflater.from(context).inflate(layoutResource, parent, false)
        view.team_logo.visibility = View.GONE
        if(list[position] is GameEventType) {
            view.team_name.text = when (list[position] as GameEventType) {
                GameEventType.GOAL -> view.resources.getString(R.string.goal)
                GameEventType.TIME_OUT -> view.resources.getString(R.string.timeout)
                GameEventType.EXCLUSION -> view.resources.getString(R.string.exclusion)
                GameEventType.YELLOW_CARD -> view.resources.getString(R.string.yellow_card)
                GameEventType.RED_CARD -> view.resources.getString(R.string.red_card)
                GameEventType.PENALTY -> view.resources.getString(R.string.penalty)
                GameEventType.MATCH_OVER -> view.resources.getString(R.string.match_over)
            }
        } else if (list[position] is MemberType){
            view.team_name.text = when (list[position]) {
                MemberType.PLAYER -> view.resources.getString(R.string.player)
                MemberType.COACH -> view.resources.getString(R.string.coach)
                MemberType.REFEREE -> view.resources.getString(R.string.referee)
                else -> ""
            }
        } else if(list[position] is String){
            view.team_number.visibility = View.VISIBLE
            view.team_number.text = "${position + 1}"
            view.team_name.text = list[position] as String
        }
        return view
    }
}