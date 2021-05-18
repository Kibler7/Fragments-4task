package com.example.domain.useCases

import com.example.domain.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetRemoteHabitsUseCase (private val habitRepository : HabitRepository,
                              private val dispatcher : CoroutineDispatcher
) {
    fun getHabit() = habitRepository.getRemoteData()

}