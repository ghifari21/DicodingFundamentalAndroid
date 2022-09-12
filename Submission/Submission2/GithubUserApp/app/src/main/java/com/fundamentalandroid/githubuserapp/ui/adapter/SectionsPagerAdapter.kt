package com.fundamentalandroid.githubuserapp.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fundamentalandroid.githubuserapp.ui.fragment.TabsFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        val fragment = TabsFragment()
        fragment.arguments = Bundle().apply {
            putInt(TabsFragment.ARG_SECTION_NUMBER, position)
            putString(TabsFragment.ARG_USERNAME, username)
        }

        return fragment
    }

    override fun getItemCount(): Int = 2
}