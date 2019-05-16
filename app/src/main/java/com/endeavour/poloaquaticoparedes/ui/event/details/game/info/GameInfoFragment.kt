package com.endeavour.poloaquaticoparedes.ui.event.details.game.info

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.endeavour.poloaquaticoparedes.databinding.GameInfoFragmentBinding
import com.endeavour.poloaquaticoparedes.ui.event.GameViewModel
import com.endeavour.poloaquaticoparedes.ui.event.details.GameDetailsFragmentArgs



class GameInfoFragment : Fragment() {

    private lateinit var binding: GameInfoFragmentBinding

    companion object {
        fun newInstance(id: Long): GameInfoFragment {
            val args : Bundle = Bundle ()
            args.putLong("id", id)
            val fragment = GameInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(GameViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = GameInfoFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = arguments?.let {
            val safeArgs = GameDetailsFragmentArgs.fromBundle(it)
            safeArgs.id
        } ?: 0L

        viewModel.gameById(id).observe(this, androidx.lifecycle.Observer { game ->

            if(game == null) return@Observer
            binding.game = game
        })
    }
}
