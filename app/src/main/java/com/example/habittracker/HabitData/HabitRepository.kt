package com.example.habittracker.HabitData

import com.example.habittracker.App
import com.example.habittracker.habitClasses.Habit


class HabitRepository{

    val habits = App.database.habitDao().getAll()

    fun addHabit(habit : Habit){
        App.database.habitDao().insert(habit)
    }

    fun deleteHabit(habit: Habit){
        App.database.habitDao().delete(habit)
    }

    fun updateHabit(habit: Habit){
        App.database.habitDao().updateHabit(habit)
    }
}