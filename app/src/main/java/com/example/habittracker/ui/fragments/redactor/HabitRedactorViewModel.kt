package com.example.habittracker.ui.fragments.redactor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitData

class HabitRedactorViewModel : ViewModel() {

    private var _color = MutableLiveData<Int>().apply {
        value = MainActivity.CONTEXT.resources.getColor(R.color.colorGreen)
    }

    fun changeColor(newColor : Int) {
        _color.value = newColor
    }

    val color : LiveData<Int> = _color


    fun addHabit(habit: Habit){
        HabitData.addHabit(habit)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(newHabit: Habit, oldHabit: Habit){
        HabitData.updateHabit(newHabit, oldHabit)
    }
}