package com.example.habittracker.ui.fragments.redactor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitData
import com.example.habittracker.habitClasses.HabitPriority
import com.example.habittracker.habitClasses.HabitType
import kotlinx.android.synthetic.main.fragment_habit_redactor.*

class HabitRedactorViewModel : ViewModel() {

    private var _color = MutableLiveData<Int>().apply {
        value = MainActivity.CONTEXT.resources.getColor(R.color.colorGreen)
    }

    val color : LiveData<Int> = _color
    var name = MutableLiveData<String>().apply {   value = ""  }
    var desription = MutableLiveData<String>().apply {    value = "" }
    var type = MutableLiveData<HabitType>().apply { value = null }
    var priority = MutableLiveData<HabitPriority>().apply { value = HabitPriority.HIGH }
    var frequency = MutableLiveData<Int>().apply { value = null }
    val frequencyText : MutableLiveData<String> = MutableLiveData<String>().apply {
        value =  when (frequency.value) {
            null -> ""
            else -> frequency.toString()
        }
    }

    var times = MutableLiveData<Int>().apply { value = null }
    val timesText : MutableLiveData<String> = MutableLiveData<String>().apply {
    value =  when (times.value) {
        null -> ""
        else -> times.toString()
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
            HabitData.addHabit(habit)
            navController.navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }


    fun saveChangedHabit(habit: Habit, navController: NavController) {
        if (validation()) {
            val newHabit = collectHabit()
            newHabit.id = habit.id
            HabitData.updateHabit(newHabit, habit)
            navController.navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }
}