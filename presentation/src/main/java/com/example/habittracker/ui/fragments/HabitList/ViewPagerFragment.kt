package com.example.habittracker.ui.fragments.HabitList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.domain.entities.HabitType
import com.example.habittracker.R
import com.example.habittracker.adapters.HabitPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.view_pager.*
import kotlinx.android.synthetic.main.view_pager.view.*


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_pager, container, false)

        val fragmentsList = arrayListOf<Fragment>(
            HabitListFragment.newInstance(HabitType.GOOD),
            HabitListFragment.newInstance(HabitType.BAD)
        )

        val adapter = HabitPagerAdapter(
            activity as AppCompatActivity,
            fragmentsList
        )

        view.viewPager.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(this.tablay, viewPager) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.viewPager_goodHabits_title)
            else
                getString(R.string.viewPager_badHabits_title)
            viewPager.setCurrentItem(tab.position, true)}.attach()
    }

}