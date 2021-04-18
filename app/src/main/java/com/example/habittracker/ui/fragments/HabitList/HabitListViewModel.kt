package com.example.habittracker.ui.fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitData
import com.example.habittracker.habitClasses.HabitType

import kotlin.collections.ArrayList


class HabitListViewModel(private val habitType: HabitType) : ViewModel(), Filterable {

    private val mutableHabit = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabit
    var habitsFilterList:  MutableLiveData<List<Habit>> = MutableLiveData()

    init {
        updateListHabits()
        habitsFilterList.value = habits.value
        HabitData.habits.observeForever( Observer { it.apply { updateListHabits() } })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val result = ( if (charSearch.isEmpty()) {
                    mutableHabit.value
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
        val habits = mutableHabit.value as MutableList
        val habit = habits[startPosition]
        habits[startPosition] = habits[newPosition]
        habits[newPosition] = habit
    }

    fun habitDeleted(habit: Habit) {
        HabitData.deleteHabit(habit)
    }

    fun sortList(position: Int){
        when(position){
            0 -> mutableHabit.value = mutableHabit.value!!.sortedBy { it.id }
            1 -> mutableHabit.value = mutableHabit.value!!.sortedBy { it.times }
            2 -> mutableHabit.value = mutableHabit.value!!.sortedBy { it.priority.value }
        }
    }

    private fun updateListHabits() {
        when (habitType) {
            HabitType.GOOD -> mutableHabit.value = HabitData.goodHabits
            HabitType.BAD -> mutableHabit.value = HabitData.badHabits
        }
    }

}
