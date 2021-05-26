package com.example.domain.useCases

import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PostHabitsUseCase (private val habitRepository : HabitRepository,
                         private val dispatcher : CoroutineDispatcher
) {
    suspend fun postHabit(habit : Habit, date : Int){
        withContext(dispatcher){
            habit.post(date)
            habitRepository.postHabit(habit, date)
        }
    }

}