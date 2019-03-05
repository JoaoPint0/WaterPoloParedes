package com.endeavour.poloaquaticoparedes.ui.athletes.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.endeavour.poloaquaticoparedes.*
import com.endeavour.poloaquaticoparedes.model.ComplexAthlete
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.endeavour.poloaquaticoparedes.model.Parent
import com.endeavour.poloaquaticoparedes.ui.athletes.AthletesViewModel
import kotlinx.android.synthetic.main.create_athlete_fragment.*
import java.util.*

class CreateAthleteFragment : Fragment() {


    private lateinit var viewModel: AthletesViewModel

    var cardId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.create_athlete_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, Injection.provideAthletesViewModelFactory(context!!))
                .get(AthletesViewModel::class.java)

        cardId = arguments?.let {
            val safeArgs = CreateAthleteFragmentArgs.fromBundle(it)
            safeArgs.cardId
        } ?: ""

        if (cardId.isNotEmpty()) {

            setupDeleteAthlete()

            athlete_card_id_input.visibility = View.GONE

            setupEditAthlete()

        } else {

            athlete_birthday.text = formatDate(Date())
        }

        athlete_birthday.setOnClickListener {

            createDatePicker(fragmentManager, it as TextView)
        }

        create_athlete_btn.setOnClickListener {

            if (cardId.isEmpty()) setupCreateAthlete() else editAthlete()
        }
    }

    private fun setupEditAthlete() {

        create_athlete_btn.text = getString(R.string.edit)

        viewModel.getAthlete().observe(this, androidx.lifecycle.Observer {

            Log.e("SetupEdit", "$it")
            athlete_name_edit.text = it.name.toEditable()
            athlete_address_edit.text = it.address.toEditable()
            athlete_postal_code_edit.text = it.postalCode.toEditable()

            athlete_birthday.text = formatDate(it.birthday)

            when (it.level) {
                Leagues.MINIPOLO -> athlete_level_toggle.setToggled(R.id.toggle_mini_polo, true)
                Leagues.SUB12 -> athlete_level_toggle.setToggled(R.id.toggle_sub12, true)
                Leagues.INFANTIL_MALE -> athlete_level_toggle.setToggled(R.id.toggle_infantil, true)
                Leagues.JUVENIL_MALE -> athlete_level_toggle.setToggled(R.id.toggle_juvenil, true)
                Leagues.A18_MALE -> athlete_level_toggle.setToggled(R.id.toggle_a18, true)
                Leagues.A20_MALE -> athlete_level_toggle.setToggled(R.id.toggle_a20, true)
                Leagues.SENIOR_FEMALE -> athlete_level_toggle.setToggled(R.id.toggle_senior, true)
            }

            when (it.gender) {
                context?.getString(R.string.male) -> athlete_sex_toggle.setToggled(R.id.toggle_male, true)
                context?.getString(R.string.female) -> athlete_sex_toggle.setToggled(R.id.toggle_female, true)
            }

            athlete_email_edit.text = it.email.toEditable()
            athlete_mobile_number_edit.text = it.mobileNumber.toString().toEditable()

            athlete_observation_edit.text = it.observations.toEditable()

            if (it.parents.isNotEmpty()) {
                parent_1_name_edit.text = it.parents[0].name.toEditable()
                parent_1_email_edit.text = it.parents[0].email.toEditable()
                parent_1_mobile_number_edit.text = it.parents[0].mobileNumber.toString().toEditable()
            }

            if (it.parents.size > 1) {
                parent_2_name_edit.text = it.parents[1].name.toEditable()
                parent_2_email_edit.text = it.parents[1].email.toEditable()
                parent_2_mobile_number_edit.text = it.parents[1].mobileNumber.toString().toEditable()
            }
        })
    }

    private fun setupDeleteAthlete() {

        delete_athlete_btn.visibility = View.VISIBLE
        delete_athlete_btn.setOnClickListener {
            deleteAthlete()
        }

    }

    private fun setupCreateAthlete() {

        if (validateFields()) viewModel.createAthlete(createComplexAthleteObject()).observe(this, androidx.lifecycle.Observer {

            if (it) {
                val bundle = Bundle()
                bundle.putString("cardId", cardId)
                findNavController().navigate(R.id.createdAthlete, bundle)
            } else {
                Toast.makeText(context, getString(R.string.create_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editAthlete() {

        if (validateFields()) viewModel.editAthlete(createComplexAthleteObject()).observe(this, androidx.lifecycle.Observer {

            if (it == true) {

                val bundle = Bundle()
                bundle.putString("cardId", cardId)
                findNavController().navigate(R.id.createdAthlete, bundle)
            } else {
                Toast.makeText(context, getString(R.string.edit_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteAthlete() {

        viewModel.deleteAthlete(cardId).observe(this, androidx.lifecycle.Observer {

            if (it == true) {

                findNavController().popBackStack(R.id.athletesFragment, false)
            } else {
                Toast.makeText(context, getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateFields(): Boolean {

        return athlete_name_edit.isNotEmpty() &&
                athlete_sex_toggle.isSelected(getString(R.string.athlete_toggle_sex_error)) &&
                athlete_level_toggle.isSelected(getString(R.string.athlete_toggle_level_error)) &&
                athlete_address_edit.isNotEmpty() &&
                athlete_mobile_number_edit.isNotEmpty() &&
                athlete_email_edit.isNotEmpty() &&
                athlete_observation_edit.isNotEmpty() &&
                athlete_postal_code_edit.isNotEmpty() &&
                parent_1_name_edit.isNotEmpty() &&
                parent_1_email_edit.isNotEmpty() &&
                parent_1_mobile_number_edit.isNotEmpty()

    }

    private fun createComplexAthleteObject(): ComplexAthlete {

        val (day, month, year) = athlete_birthday.text.toString().split("/").map { it.toInt() }
        if (cardId.isEmpty()) cardId = athlete_card_id_edit.text.toString()

        return ComplexAthlete(athlete_name_edit.text.toString(),
                athlete_address_edit.text.toString(),
                athlete_postal_code_edit.text.toString(),
                Date(year - 1900, month - 1, day),
                athlete_sex_toggle.selectedToggles()[0].title.toString(),
                athlete_mobile_number_edit.text.toString().toLong(),
                listOf(Parent(parent_1_name_edit.text.toString(),
                        parent_1_email_edit.text.toString(),
                        parent_1_mobile_number_edit.text.toString().toLong()),
                        Parent(parent_2_name_edit.text.toString(),
                                parent_2_email_edit.text.toString(),
                                if (parent_2_mobile_number_edit.text.toString().isEmpty()) 0L else parent_2_mobile_number_edit.text.toString().toLong())),
                listOf(Date().year - 1, Date().year),
                athlete_email_edit.text.toString(),
                cardId.toLong(),
                "m",
                Leagues.valueOf(athlete_level_toggle.selectedToggles()[0].title.toString().toUpperCase()),
                athlete_observation_edit.text.toString(),
                true,
                true,
                false)
    }
}