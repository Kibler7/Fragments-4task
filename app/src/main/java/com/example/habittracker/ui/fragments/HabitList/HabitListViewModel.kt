package com.example.habittracker.ui.fragments.HabitList

import android.os.Bundle
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.habittracker.HabitData.HabitRepository
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitType
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment


class HabitListViewModel(private val habitType: HabitType) : ViewModel(), Filterable {

    lateinit var navController: NavController

    private val mutableHabitList = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabitList
    private var repository = HabitRepository()


    init {
        repository.habits.observeForever(Observer { list ->
            mutableHabitList.value = list.filter { it.type == habitType }
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val result = FilterResults()
                if (charSearch.isEmpty())
                    result.values = mutableHabitList.value
                result.values = mutableHabitList.value!!.filter { it.name.startsWith(charSearch) }
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabitList.value = results?.values as List<Habit>?
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
        repository.deleteHabit(habit)
    }

    fun sortList(position: Int){
        when(position){
            0 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.id }
            1 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.name }
            2 -> mutableHabitList.value = mutableHabitList.value!!.sortedBy { it.priority.value }
        }
    }


    fun createNewHabit() {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.ADD_HABIT_KEY)
        navController.navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

    fun changeHabit(habit: Habit) {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.CHANGE_HABIT_KEY)
        bundle.putSerializable(HabitRedactorFragment.HABIT_KEY, habit)
        navController.navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

}
