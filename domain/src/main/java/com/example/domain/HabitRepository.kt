package com.example.domain

import androidx.lifecycle.LiveData
import com.example.domain.entities.Habit

public interface HabitRepository {

    fun addHabit(habit: Habit)

    fun deleteHabit(habit : Habit)

    fun updateHabit(habit: Habit)

    fun getLocalData() : LiveData<List<Habit>>

    fun getRemoteData() : List<Habit>?

}