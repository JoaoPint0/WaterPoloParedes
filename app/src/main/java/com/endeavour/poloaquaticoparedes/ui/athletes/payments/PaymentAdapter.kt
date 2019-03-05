package com.endeavour.poloaquaticoparedes.ui.athletes.payments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.model.Payment

class PaymentAdapter : RecyclerView.Adapter<PaymentCardViewHolder>() {

    var cardId = 0L
    var season = 0
    private var paymentsList: List<Payment> = emptyList()

    fun setPayments(payments: List<Payment>) {
        paymentsList = payments
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PaymentCardViewHolder, position: Int) {

        holder.cardId = cardId
        holder.season = season

        if (paymentsList.isNotEmpty()) {
            val payment = paymentsList[position]
            holder.bind(payment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentCardViewHolder {
        return PaymentCardViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return paymentsList.size
    }

    fun getPayments(): List<Payment> {
        return paymentsList
    }
}