package com.example.domain.useCases

import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddHabitUseCase(private val habitDBRepository: HabitRepository,
                      private val dispatcher : CoroutineDispatcher) {

    suspend fun addHabit(habit: Habit){
        withContext(dispatcher){
            habitDBRepository.addHabit(habit)
        }
    }
}