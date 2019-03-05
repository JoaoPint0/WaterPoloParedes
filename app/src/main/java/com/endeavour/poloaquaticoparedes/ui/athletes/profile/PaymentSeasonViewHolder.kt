package com.endeavour.poloaquaticoparedes.ui.athletes.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.R
import kotlinx.android.synthetic.main.payment_season_item.view.*
import kotlinx.android.synthetic.main.payments_view_item.view.*

class PaymentSeasonViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(cardId:String, season: Int?) {
        if (season == null) {
            val resources = itemView.resources
            view.payment_status.text = resources.getString(R.string.loading)
        } else {
            showRepoData(cardId, season)
        }
    }

    private fun showRepoData(cardId:String, season: Int) {

        view.payment_btn.text = "Season $season/${season + 1}"
        view.payment_btn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("cardId", cardId)
            bundle.putString("year", season.toString())
            Navigation.findNavController(it).navigate(R.id.paymentsFragment, bundle)
        }
    }

    companion object {
        fun create(parent: ViewGroup): PaymentSeasonViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.payment_season_item, parent, false)
            return PaymentSeasonViewHolder(view)
        }
    }
}