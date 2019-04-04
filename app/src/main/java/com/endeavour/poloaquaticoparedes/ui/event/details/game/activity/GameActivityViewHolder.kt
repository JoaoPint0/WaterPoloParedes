package com.endeavour.poloaquaticoparedes.ui.event.details.game.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.formatCounter
import com.endeavour.poloaquaticoparedes.formatTime
import com.endeavour.poloaquaticoparedes.model.GameEvent
import com.endeavour.poloaquaticoparedes.model.GameEventType
import kotlinx.android.synthetic.main.game_activity_view_item.view.*

class GameActivityViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    fun showData(gameEvent: GameEvent, isNewPeriod: Boolean) {

        if(gameEvent.team == 0){
            view.activity_home_name.visibility = View.VISIBLE
            view.activity_home_name.text = if(gameEvent.type == GameEventType.TIME_OUT) view.resources.getString(R.string.timeout) else  gameEvent.playerName
            setIcon(view.activity_home_icon, gameEvent.type)
            setComment(view.activity_home_comment, gameEvent.type)
            view.activity_away_icon.visibility = View.GONE
            view.activity_away_name.visibility = View.GONE
            view.activity_away_comment.visibility = View.GONE
        }else {
            view.activity_away_name.visibility = View.VISIBLE
            view.activity_away_name.text = if(gameEvent.type == GameEventType.TIME_OUT) view.resources.getString(R.string.timeout) else  gameEvent.playerName
            setIcon(view.activity_away_icon, gameEvent.type)
            setComment(view.activity_away_comment, gameEvent.type)
            view.activity_home_icon.visibility = View.GONE
            view.activity_home_name.visibility = View.GONE
            view.activity_home_comment.visibility = View.GONE
        }

        view.activity_time.text = formatCounter(gameEvent.time)

        if(isNewPeriod){
            view.activity_round_margin.visibility = View.VISIBLE
            view.activity_new_round.apply {
                visibility = View.VISIBLE
                text = "Round ${gameEvent.period}"
            }
        }else {
            view.activity_round_margin.visibility = View.GONE
            view.activity_new_round.visibility = View.GONE
        }
    }

    private fun setIcon(image: ImageView, type: GameEventType){
        image.visibility = View.VISIBLE

        Glide.with(view)
            .load(when(type){
                GameEventType.TIME_OUT -> ""
                GameEventType.MATCH_OVER -> ""
                GameEventType.RED_CARD -> view.resources.getDrawable(R.drawable.ic_red_card)
                GameEventType.YELLOW_CARD -> view.resources.getDrawable(R.drawable.ic_yellow_card)
                GameEventType.GOAL -> view.resources.getDrawable(R.drawable.ic_waterpolo_ball)
                GameEventType.EXCLUSION -> ""
                GameEventType.PENALTY -> view.resources.getDrawable(R.drawable.ic_penalty)
            })
            .apply(RequestOptions().centerCrop())
            .into(image)
    }

    private fun setComment(view: TextView, type: GameEventType){

        view.apply {
            visibility = View.VISIBLE
            text = when(type){
                GameEventType.TIME_OUT -> {
                    ""
                }
                GameEventType.MATCH_OVER -> {
                    ""
                }
                GameEventType.RED_CARD -> view.resources.getString(R.string.red_card)
                GameEventType.YELLOW_CARD -> view.resources.getString(R.string.yellow_card)
                GameEventType.GOAL -> view.resources.getString(R.string.goal)
                GameEventType.EXCLUSION -> view.resources.getString(R.string.exclusion)
                GameEventType.PENALTY -> view.resources.getString(R.string.penalty)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): GameActivityViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_activity_view_item, parent, false)
            return GameActivityViewHolder(view)
        }
    }
}
