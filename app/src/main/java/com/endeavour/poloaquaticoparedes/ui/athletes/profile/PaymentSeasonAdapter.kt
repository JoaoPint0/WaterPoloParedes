package com.endeavour.poloaquaticoparedes.ui.athletes.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PaymentSeasonAdapter : RecyclerView.Adapter<PaymentSeasonViewHolder>() {

    private var seasonsList: List<Int> = emptyList()
    private var cardId =  ""

    fun setCardId(id:String){
        cardId = id
    }

    fun setSeasonsList(seasons: List<Int>) {
        seasonsList = seasons
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PaymentSeasonViewHolder, position: Int) {

        if (seasonsList.isNotEmpty()) {
            val payment = seasonsList[position]
            holder.bind(cardId, payment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentSeasonViewHolder {
        return PaymentSeasonViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return seasonsList.size
    }
}