package com.example.habittracker.habitClasses

import android.os.Build
import androidx.annotation.RequiresApi

object HabitData {

    private var habits = mutableMapOf<Long, Habit>()

    val goodHabits = mutableListOf<Habit>()
    val badHabits = mutableListOf<Habit>()

    fun generateId() = habits.size.toLong()

    fun addHabit(habit: Habit) {
        habits[generateId()] = habit
        when (habit.type) {
            HabitType.GOOD -> goodHabits.add(habit)
            HabitType.BAD -> badHabits.add(habit)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(newHabit: Habit, id: Long) {
        newHabit.id = id
        if (habits[id]!!.type != newHabit.type) {
            habits[id] = newHabit
            when (newHabit.type) {
                HabitType.GOOD ->{
                    badHabits.removeIf { it.id == id }
                    goodHabits.add(newHabit)
                }
                HabitType.BAD -> {
                    goodHabits.removeIf { it.id == id }
                    badHabits.add(newHabit)
                }
            }

        } else {
            habits[id] = newHabit
            when (newHabit.type) {
                HabitType.GOOD -> goodHabits[goodHabits.indexOfFirst { it.id == id }] =
                    newHabit
                HabitType.BAD -> badHabits[badHabits.indexOfFirst { it.id == id }] = newHabit
            }
        }
    }

}