package com.example.bookkeeper.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BookkeepFragment()
            1 -> AccountFragment()
            else -> ChartFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}