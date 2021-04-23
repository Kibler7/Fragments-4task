package com.example.habittracker.ui.fragments.HabitList

import android.os.Bundle
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitData
import com.example.habittracker.habitClasses.HabitType
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment

import kotlin.collections.ArrayList


class HabitListViewModel(private val habitType: HabitType) : ViewModel(), Filterable {

    private val mutableHabitList = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabitList
    var habitsFilterList:  MutableLiveData<List<Habit>> = MutableLiveData()

    init {
        updateHabitList()
        habitsFilterList.value = habits.value
        HabitData.habits.observeForever( Observer {
            it.apply {
                updateHabitList()
            }
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val result = ( if (charSearch.isEmpty()) {
                    mutableHabitList.value
                } else {
                    val resultList = ArrayList<Habit>()
                    habits.value!!.forEach {
                        if (it.name.startsWith(charSearch))
                            resultList.add(it)
                    }
                    resultList.toList()
                })
                val filterResults = FilterResults()
                filterResults.values = result
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                habitsFilterList.value = results?.values as List<Habit>?
            }
        }
    }

    fun getItems() = habits.value

    fun habitsMoved(startPosition: Int, newPosition: Int) {
        val habits = mutableHabitList.value as MutableList
        val habit = habits[startPosition]
        habits[startPosition] = habits[newPosition]
        habits[newPosition] = habit
    }

    fun habitDeleted(habit: Habit) {
        HabitData.deleteHabit(habit)
    }

    fun sortList(position: Int){
        when(position){
            0 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.id }
            1 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.name }
            2 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.priority.value }
        }
    }

    private fun updateHabitList() {
        when (habitType) {
            HabitType.GOOD -> mutableHabitList.value = HabitData.goodHabits
            HabitType.BAD -> mutableHabitList.value = HabitData.badHabits
        }
    }

    fun createNewHabit(navController: NavController) {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.ADD_HABIT_KEY)
        navController.navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

    fun changeHabit(habit: Habit, navController: NavController) {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.CHANGE_HABIT_KEY)
        bundle.putSerializable(HabitRedactorFragment.HABIT_KEY, habit)
        navController.navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

}
