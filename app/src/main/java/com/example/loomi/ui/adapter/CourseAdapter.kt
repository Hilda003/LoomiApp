package com.example.loomi.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loomi.ui.content.DescriptionFragment
import com.example.loomi.ui.material.MaterialFragment


class CoursePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MaterialFragment()
            1 -> DescriptionFragment()
            else -> throw IllegalArgumentException("Invalid tab index")
        }
    }
}
