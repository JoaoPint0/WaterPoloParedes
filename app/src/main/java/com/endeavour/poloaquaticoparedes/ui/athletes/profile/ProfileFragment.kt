package com.endeavour.poloaquaticoparedes.ui.athletes.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.Parent
import com.endeavour.poloaquaticoparedes.ui.athletes.AthletesViewModel
import kotlinx.android.synthetic.main.parent_view.view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.season_picker_dialog.view.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: AthletesViewModel
    private var cardId = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, Injection.provideAthletesViewModelFactory(context!!))
            .get(AthletesViewModel::class.java)

        cardId = arguments?.let {
            val safeArgs = ProfileFragmentArgs.fromBundle(it)
            safeArgs.cardId
        } ?: ""

        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        if (cardId == "0") {
            cardId = sharedPref?.getString(getString(R.string.card_id), "") ?: "0"
        }

        updateProfile()
        observeProfile()

        if (sharedPref?.getString(getString(R.string.privileges), "") == "admin") setupEditAthlete(cardId)

        new_payment_season.setOnClickListener { showSeasonPicker() }

    }

    private fun updateProfile() {

        viewModel.updateAthlete(cardId)
    }

    private fun showSeasonPicker() {

        val builder = AlertDialog.Builder(context!!)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.season_picker_dialog, null)

        val profileFragment = this

        builder.apply {

            setView(view)
            setPositiveButton("Confirm ") { _, _ ->

                viewModel.createMonthPayment(cardId.toLong(), view.season_picker.value, null).observe(profileFragment, Observer {

                    if (it) updateProfile()
                })
            }
            setNegativeButton("Cancel") { _, _ ->

            }
        }.show()

        view.season_picker.maxValue = 2020
        view.season_picker.minValue = 2010
        view.season_picker.value = 2018
    }

    private fun observeProfile() {

        viewModel.getAthlete().observe(this, Observer {

            if(it != null) {
                athlete_name.text = it.name
                athlete_level.text = getLeagueText(context, it.level)

                athlete_birthday.text = formatDate(it.birthday)
                athlete_adress.text = it.address
                athlete_postal_code.text = it.postalCode
                athlete_email.text = it.email

                athlete_observations.text = it.observations

                setupPhoneCall(athlete_mobile_number, it.mobileNumber)
                setupSendEmail(athlete_email, it.email)

                setupParentsList(it.parents)
                setupPaymentsYear(cardId, it.yearsPaid)
            }
        })
    }

    private fun setupEditAthlete(id: String) {

        edit_athlete.visibility = View.VISIBLE
        edit_athlete.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("cardId", id)
            Navigation.findNavController(it).navigate(R.id.createAthleteFragment, bundle)
        }
    }

    private fun setupPaymentsYear(cardId: String, yearsPaid: List<Int>) {

        val adapter = PaymentSeasonAdapter()

        athlete_payments_years_list.adapter = adapter
        athlete_payments_years_list.layoutManager = GridLayoutManager(context, 2)

        adapter.setCardId(cardId)
        adapter.setSeasonsList(yearsPaid)
    }


    private fun setupParentsList(parents: List<Parent>) {

        parents_layout.removeAllViews()

        if(parents.isEmpty()){
            parents_card.visibility = View.GONE
            return
        }

        parents.forEach {

            val child = layoutInflater.inflate(R.layout.parent_view, null)

            child.parent_name.text = it.name
            setupPhoneCall(child.parent_mobile_number, it.mobileNumber)
            setupSendEmail(child.parent_email, it.email)

            parents_layout.addView(child)
        }
    }

    private fun setupSendEmail(textView: TextView, email: String) {

        textView.text = email
        textView.emailIntentListener()
    }

    private fun setupPhoneCall(textView: TextView, mobileNumber: Long) {

        textView.text = mobileNumber.toString()
        textView.callIntentListener()
    }

}
