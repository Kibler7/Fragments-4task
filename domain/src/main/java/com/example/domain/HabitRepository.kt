package com.example.domain

import com.example.domain.entities.Habit
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

public interface HabitRepository {

    fun addHabit(habit: Habit)

    fun deleteHabit(habit : Habit)

    fun updateHabit(habit: Habit)

    fun getHabitsData() : Flow<List<Habit>>

    fun postHabit(habit : Habit, date : Int) : Job

}