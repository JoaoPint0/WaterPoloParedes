package com.endeavour.poloaquaticoparedes.ui.event.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import com.endeavour.poloaquaticoparedes.isNotEmpty
import com.endeavour.poloaquaticoparedes.model.Team
import com.endeavour.poloaquaticoparedes.ui.game.GameViewModel
import kotlinx.android.synthetic.main.create_team_dialog.*
import kotlinx.android.synthetic.main.create_team_dialog.view.*

class CreateTeamDialogFragment : DialogFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this, Injection.provideGameViewModelFactory(context!!)).get(GameViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.create_team_dialog, container)

        view.team_logo_edit.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Glide.with(view.context)
                    .load(s.toString())
                    .apply(RequestOptions().centerCrop())
                    .into(view.dialog_team_logo)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        view.create_team_btn.setOnClickListener {
            if (isTeamValid()){
                viewModel.createTeam(getTeam()).observe(this, Observer {
                    if (it.success) dismiss() else Toast.makeText(context, "Error Creating Team", Toast.LENGTH_LONG).show()
                })
            }
        }
        return view
    }

    private fun isTeamValid(): Boolean {
        return team_logo_edit.isNotEmpty() && team_name_edit.isNotEmpty() && team_logo_edit.isNotEmpty()
    }

    private fun getTeam(): Team {
        return Team(team_name_edit.text.toString(),
            team_acronym_edit.text.toString(),
            team_logo_edit.text.toString(),
            emptyList(),
            emptyList())
    }
}