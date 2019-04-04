package com.endeavour.poloaquaticoparedes.ui.lobby

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.endeavour.poloaquaticoparedes.R;
import com.endeavour.poloaquaticoparedes.getLeagueText
import com.endeavour.poloaquaticoparedes.model.Leagues
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import kotlinx.android.synthetic.main.fragment_leagues_list_dialog.*
import kotlinx.android.synthetic.main.fragment_leagues_list_dialog_item.view.*

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    LeaguesListDialogFragment.newInstance().show(supportFragmentManager, "dialog")
 * </pre>
 */
class LeaguesListDialogFragment : BottomSheetDialogFragment(){

    private lateinit var viewModel: GameViewModel
    private val sharedPref  by lazy { context!!.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_leagues_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        leagues_list.layoutManager = GridLayoutManager(context, 2)
        leagues_list.adapter = LeaguesAdapter()
    }

    fun setViewModel(viewModel: GameViewModel) {
        this.viewModel = viewModel
    }

    override fun onDestroy() {

        viewModel.rearrangeLobby(sharedPref.getStringSet("filter_leagues", Leagues.values().map { it.name }.toSet()) ?: emptySet())
        super.onDestroy()
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_leagues_list_dialog_item, parent, false)) {

        fun show(league: Leagues) {

            val filters = sharedPref.getStringSet("filter_leagues", Leagues.values().map { it.name }.toSet()) ?: emptySet()
            var checked = filters.contains(league.name)

            itemView.radio_button.text = getLeagueText(context, league)
            itemView.radio_button.isChecked = checked
            itemView.setOnClickListener {
                checked = !checked
                itemView.radio_button.isChecked = checked

            }
            itemView.radio_button.setOnCheckedChangeListener { _ , isChecked ->
                sharedPref.edit().apply {
                    if (isChecked) filters.add(league.name) else filters.remove(league.name)
                    putStringSet("filter_leagues", filters).apply()
                }
            }
        }
    }

    private inner class LeaguesAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.show(Leagues.values()[position])
        }

        override fun getItemCount(): Int {
            return Leagues.values().size
        }
    }

    companion object {

        fun newInstance(): LeaguesListDialogFragment = LeaguesListDialogFragment()
    }
}
