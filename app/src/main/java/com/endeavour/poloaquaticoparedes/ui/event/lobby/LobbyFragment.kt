package com.endeavour.poloaquaticoparedes.ui.event.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.endeavour.poloaquaticoparedes.R
import kotlinx.android.synthetic.main.lobby_fragment.*

class LobbyFragment : Fragment() {

    private val adapter = LeaguesListAdapter()

    private lateinit var viewModel: LobbyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.lobby_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LobbyViewModel::class.java)

        categories_list.adapter = adapter
        categories_list.layoutManager = GridLayoutManager(context, 2)
    }

}
