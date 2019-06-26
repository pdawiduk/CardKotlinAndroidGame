package com.example.myapplication.activities.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myapplication.R
import com.example.myapplication.restApiUtils.ModelCard




class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private val cardsList: List<ModelCard>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val card:ModelCard = cardsList.get(position)
        return PlaceholderFragment.newInstance(card.image,card.value)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun getCount(): Int {
        // Show 2 total pages.

        return cardsList.size
    }
}