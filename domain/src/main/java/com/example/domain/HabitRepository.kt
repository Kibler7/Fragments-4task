package com.example.domain

import androidx.lifecycle.LiveData
import com.example.domain.entities.Habit
import kotlinx.coroutines.Job

public interface HabitRepository {

    fun addHabit(habit: Habit)

    fun deleteHabit(habit : Habit)

    fun updateHabit(habit: Habit)

    fun getLocalData() : LiveData<List<Habit>>

    fun postHabit(habit : Habit, date : Int) : Job

}