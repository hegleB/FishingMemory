package com.qure.create.location

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LocationSettingPagerAdapter(
    fragmentActivity: FragmentActivity,
    var fragments: MutableList<Fragment>,
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    fun refreshFragment(index: Int, fragment: Fragment) {
        fragments[index] = fragment
        notifyItemChanged(index)
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}