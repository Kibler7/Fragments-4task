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


    private val mutableHabitList = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabitList
    private var repository = HabitRepository()
    private var notFilteredList = mutableHabitList.value


    init {
        onCreate()
    }

    private lateinit var observer: Observer<List<Habit>>
    private fun onCreate() {
        observer = Observer {
            mutableHabitList.value = it.filter { el -> el.type == habitType }
            notFilteredList = mutableHabitList.value
        }
        repository.habits.observeForever(observer)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()
                val searchResult = FilterResults()
                if (searchString.isEmpty())
                    searchResult.values = notFilteredList
                else
                    searchResult.values = mutableHabitList.value!!.filter { it.name.contains(searchString) }
                return searchResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabitList.value = results?.values as? List<Habit>?
            }
        }
    }

    fun getHabits() = habits.value


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
}
