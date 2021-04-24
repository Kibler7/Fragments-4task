package com.example.habittracker.ui.fragments.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.habittracker.App
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitPriority
import com.example.habittracker.habitClasses.HabitType

class HabitRedactorViewModel : ViewModel() {

    private  var dataSize = 0
    init {
        App.database.habitDao().getAll().observeForever {
            dataSize = it.size
        }
    }


    private var _color = MutableLiveData<Int>().apply {
        value = MainActivity.CONTEXT.resources.getColor(R.color.colorGreen)
    }

    val color : LiveData<Int> = _color
    var name = MutableLiveData<String>().apply {   value = ""  }
    var desription = MutableLiveData<String>().apply {    value = "" }
    var type = MutableLiveData<HabitType>().apply { value = null }
    var priority = MutableLiveData<HabitPriority>().apply { value = HabitPriority.HIGH }

    val frequencyText : MutableLiveData<String> = MutableLiveData<String>()
    var frequency = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            frequencyText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }

    val timesText : MutableLiveData<String> = MutableLiveData<String>()
    var times = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            timesText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }


    fun getPriority(position : Int){
        when (position){
            0 -> priority.value = HabitPriority.HIGH
            1 -> priority.value = HabitPriority.MEDIUM
            2 -> priority.value = HabitPriority.LOW
        }
    }

    var isEnteredName = MutableLiveData<Boolean>().apply { value = true }
    var isSelectedType = MutableLiveData<Boolean>().apply { value = true }
    var isEnteredFrequency = MutableLiveData<Boolean>().apply { value = true }
    var isNeededErrorText = MutableLiveData<Boolean>().apply { value = false }

    fun updateHabitData(habit: Habit) {
        name.value = habit.name
        desription.value = habit.description
        priority.value = habit.priority
        frequency.value = habit.period
        times.value = habit.times
        type.value = habit.type
        changeColor(habit.color)
    }


    private fun validation(): Boolean {
        var allFieldsFilled = true

        if (name.value!!.isEmpty()){
            allFieldsFilled = false
            isEnteredName.value = false
        }
        else
            isEnteredName.value = true

        if (type.value == null){
            allFieldsFilled = false
            isSelectedType.value = false
        }
        else
            isSelectedType.value = true
        if (times.value == null ||
                frequency.value == null){
            allFieldsFilled = false
            isEnteredFrequency.value = false
        }
        else
            isEnteredFrequency.value = true

        if (!allFieldsFilled)
            isNeededErrorText.value = true

        return allFieldsFilled
    }

    private fun collectHabit(): Habit {
        return Habit(-1, name.value!!, desription.value!!, type.value!!,
                priority.value!!, times.value!!, frequency.value!!, color.value!!)
    }

    fun changeColor(newColor : Int) {
        _color.value = newColor
    }


    fun saveNewHabit(navController: NavController) {
        if (validation()) {
            val habit = collectHabit()
            habit.id = dataSize.toLong()
            App.database.habitDao().insert(habit)
            navController.navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }


    fun saveChangedHabit(habit: Habit, navController: NavController) {
        if (validation()) {
            val newHabit = collectHabit()
            newHabit.id = habit.id
            App.database.habitDao().updateHabit(newHabit)
            navController.navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }
}