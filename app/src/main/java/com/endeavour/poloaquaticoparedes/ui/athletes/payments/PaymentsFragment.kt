package com.endeavour.poloaquaticoparedes.ui.athletes.payments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.model.Payment
import com.endeavour.poloaquaticoparedes.ui.athletes.AthletesViewModel
import kotlinx.android.synthetic.main.payments_fragment.*

class PaymentsFragment : Fragment() {

    private lateinit var viewModel: AthletesViewModel

    private val adapter = PaymentAdapter()

    private var cardId = 0L
    private var year = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.payments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, Injection.provideAthletesViewModelFactory(context!!))
                .get(AthletesViewModel::class.java)

        arguments?.let {
            val safeArgs = PaymentsFragmentArgs.fromBundle(it)
            cardId = safeArgs.cardId.toLong()
            year = safeArgs.year.toInt()
        }

        adapter.cardId = cardId
        adapter.season = year

        payment_season_title.text = activity?.getString(R.string.payment_season_headline,year, year+1)

        payment_year_list.adapter = adapter
        payment_year_list.layoutManager = GridLayoutManager(context, context!!.resources.getInteger(R.integer.grid_column_count))

        viewModel.getAthleteSeasonPayments(cardId, year).observe(this, Observer {

            if (it != null) {
                Log.e("PaymentsFragment", "$it")
                adapter.setPayments(it)
                showErrorLoadingPayments(it.isEmpty())

                setSeasonPayment()
            }
        })

        viewModel.refresh()
    }

    private fun setSeasonPayment() {

        season_payment_btn.setOnClickListener { _ ->

            Log.e("PaymentsFragment", "season btn")
            validateSeasonPayment()

        }
    }

    private fun validateSeasonPayment() {

        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(getString(R.string.payment_season))
                .setPositiveButton("Pay") { _, _ ->

                    observeSeasonPayment()
                }
                .setNegativeButton("Cancel") { _, _ ->

                    Log.e("Cancel", "Season payment")
                }
        builder.show()
    }

    private fun observeSeasonPayment(){
        val payments = mutableListOf<Payment>()

        for (i in 8..11){

            payments.add(Payment(i,20.0))
        }

        for (i in 0..7){

            payments.add(Payment(i,20.0))
        }

        viewModel.createSeasonPayment(cardId, year, payments).observe(this, Observer {

            if(it){
                adapter.setPayments(payments)
            }else{
                Toast.makeText(context!!, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showErrorLoadingPayments(isEmpty: Boolean) {

        if (isEmpty) {
            payment_year_list.visibility = View.GONE
            season_payment_btn.visibility = View.GONE
            loading_payments.visibility = View.VISIBLE
        } else {
            payment_year_list.visibility = View.VISIBLE
            season_payment_btn.visibility = View.VISIBLE
            loading_payments.visibility = View.GONE
        }
    }
}
