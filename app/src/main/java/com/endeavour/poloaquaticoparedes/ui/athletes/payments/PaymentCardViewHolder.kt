package com.endeavour.poloaquaticoparedes.ui.athletes.payments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.endeavour.poloaquaticoparedes.Injection.provideRepository
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.CreatePayment
import com.endeavour.poloaquaticoparedes.model.Payment
import kotlinx.android.synthetic.main.payments_view_item.view.*
import java.text.DateFormatSymbols

class PaymentCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    var cardId = 0L
    var season = 0

    fun bind(payment: Payment?) {
        if (payment == null) {
            val resources = itemView.resources
            view.payment_status.text = resources.getString(R.string.loading)
        } else {
            showRepoData(payment)
        }
    }

    private fun showRepoData(payment: Payment) {

        view.payment_month.text = DateFormatSymbols().months[payment.month]
        view.payment_status.text = view.resources.getString(when {
            payment.month == 7 -> R.string.no_payment
            payment.value <= 0 -> R.string.payment_missing
            payment.value > 0 -> R.string.payment_paid
            else -> R.string.unknown_payment
        })

        view.payment_status.setTextColor(ContextCompat.getColor(view.context,
                when {
                    payment.month == 7 -> R.color.yellow
                    payment.value <= 0 -> R.color.red
                    payment.value > 0 -> R.color.green
                    else -> R.color.gray
                }))

        view.setOnClickListener {

            when {
                payment.month == 7 -> Log.e ("paymentViewHolder", "August")
                payment.value <= 0 -> validatePayment(payment.month)
                payment.value > 0 -> Log.e ("paymentViewHolder", "Already Payed")
                else -> R.string.unknown_payment
            }
        }
    }

    private fun validatePayment(month : Int) {
        val builder = AlertDialog.Builder(view.context)
        builder.setMessage(view.context.getString(R.string.confirm_payment, DateFormatSymbols().months[month]))
                .setPositiveButton("Pay") { _, _ ->

                    doMonthPayment(month)

                }
                .setNegativeButton("Cancel") { _, _ ->

                }
        builder.show()
    }

    private fun doMonthPayment(month: Int) {

        val repository = provideRepository(view.context)

        val monthPayment = CreatePayment(cardId, season, listOf(Payment(month, 15.0)))

        repository.createPayment(monthPayment)

        view.payment_status.text = view.resources.getString(R.string.payment_paid)
        view.payment_status.setTextColor(ContextCompat.getColor(view.context,R.color.green))
    }

    companion object {
        fun create(parent: ViewGroup): PaymentCardViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.payments_view_item, parent, false)
            return PaymentCardViewHolder(view)
        }
    }
}