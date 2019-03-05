package com.endeavour.poloaquaticoparedes.ui.athletes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.endeavour.poloaquaticoparedes.Injection
import com.endeavour.poloaquaticoparedes.R
import kotlinx.android.synthetic.main.athletes_fragment.*

class AthletesFragment : Fragment() {

    private lateinit var viewModel: AthletesViewModel

    private val adapter = AthleteListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.athletes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, Injection.provideAthletesViewModelFactory(context!!))
                .get(AthletesViewModel::class.java)

        athletes_list.adapter = adapter
        athletes_list.layoutManager = LinearLayoutManager(context)


        viewModel.athletes.observe(this, Observer {

            adapter.setAthletesList(it)
            showErrorLoadingPayments(it.isEmpty())
        })

        create_athlete.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardId","")
            Navigation.findNavController(it).navigate(R.id.createAthleteFragment,bundle)
        }

        setupSearchCardClick()
        setupAthleteSearch()

        viewModel.searchAthlete("")
    }

    private fun setupAthleteSearch() {
        search_view_athletes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                viewModel.searchAthlete(newText)
                return false
            }

            override fun onQueryTextSubmit(search: String): Boolean {

                viewModel.searchAthlete(search)
                return false
            }
        })
    }

    private fun setupSearchCardClick() {
        search_card_athletes.setOnClickListener {

            search_view_athletes.isIconified = false
            search_view_athletes.requestFocus()
        }
    }

    private fun showErrorLoadingPayments(isEmpty: Boolean) {

        if (isEmpty) {
            athletes_list.visibility = View.GONE
            loading_athletes.visibility = View.VISIBLE
            loading_athletes.text = activity?.getString(R.string.empty_list) ?: ""
        } else {
            athletes_list.visibility = View.VISIBLE
            loading_athletes.visibility = View.GONE
        }
    }
}
