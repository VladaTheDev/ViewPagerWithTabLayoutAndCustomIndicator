package com.vladadev.viewpagerwithtablayoutandcustomindicator

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val PAGES_SIZE = 3

class OnBoardingAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = PAGES_SIZE

    override fun createFragment(position: Int): Fragment {
        return BlankFragment()
    }
}