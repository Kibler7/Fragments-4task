package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.NewItemTouchHelper
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitData
import com.example.habittracker.habitClasses.HabitType
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment
import kotlinx.android.synthetic.main.fragment_habit_list.*

class HabitListFragment : Fragment() {

    companion object {
        const val HABIT_TYPE = "habit_type"
        const val RESULT_NEW_HABIT = 0
        const val RESULT_CHANGED_HABIT = 1
        const val RESULT = "result"

        fun newInstance(habitType: HabitType) : HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener {
            addHabit()
        }

        val habitList =
                when (this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE)) {
                    HabitType.GOOD -> HabitData.goodHabits
                    else -> HabitData.badHabits
        }
        addAdapter(habitList)
    }

    private fun addAdapter(habitList: MutableList<Habit>) {
        habit_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HabitAdapter(habitList)
                { habit ->
                    changeHabit(habit)
                }
        }
        habit_list.adapter!!.notifyDataSetChanged()
        val callback: ItemTouchHelper.Callback = NewItemTouchHelper(habit_list.adapter as HabitAdapter)
        val newItemTouchHelper = ItemTouchHelper(callback)
        newItemTouchHelper.attachToRecyclerView(habit_list)
    }
    


    private fun addHabit() {
        val bundle = Bundle()
        bundle.apply {
            putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.ADD_HABIT_KEY)
        }
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

    private fun changeHabit(habit: Habit) {
        val bundle = Bundle()
        bundle.apply {
            putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.CHANGE_HABIT_KEY)
            putSerializable(HabitRedactorFragment.HABIT_KEY, habit)
        }
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }
}