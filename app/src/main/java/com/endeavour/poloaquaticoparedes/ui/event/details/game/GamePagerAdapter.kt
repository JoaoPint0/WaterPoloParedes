package com.endeavour.poloaquaticoparedes.ui.event.details.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GamePagerAdapter(fm: FragmentManager, private val list: List<Fragment>, private val names: List<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return names[position]
    }

    override fun getCount(): Int {

        return list.size
    }
}