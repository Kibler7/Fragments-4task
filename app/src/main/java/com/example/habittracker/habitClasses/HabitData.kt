package com.example.habittracker.habitClasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object HabitData : LiveData<Habit>() {
    private val mutableHabits = MutableLiveData<LinkedHashMap<Long, Habit>>().apply {
        value = LinkedHashMap()
    }
    var habits: LiveData<LinkedHashMap<Long, Habit>> = mutableHabits


    val goodHabits: List<Habit>
        get() = habits.value!!.filter { it.value.type == HabitType.GOOD }.values.toList()

    val badHabits: List<Habit>
        get() = habits.value!!.filter { it.value.type == HabitType.BAD }.values.toList()

    fun addHabit(habit: Habit) {
        val newId = habits.value!!.size.toLong()
        habit.id = newId
        mutableHabits.value!![newId] = habit
    }

    fun updateHabit(newHabit: Habit, oldHabit : Habit) {
        newHabit.id = oldHabit.id
        mutableHabits.value!![oldHabit.id] = newHabit
    }

    fun deleteHabit(habit: Habit) {
        mutableHabits.value =  mutableHabits.value!!.apply {
            remove(habit.id)
        }
    }
}