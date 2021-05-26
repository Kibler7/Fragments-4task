package com.example.domain.useCases

import com.example.domain.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetHabitsUseCase(private val habitRepository : HabitRepository,
                       private val dispatcher : CoroutineDispatcher) {


    fun getHabit() = habitRepository.getLocalData()

}