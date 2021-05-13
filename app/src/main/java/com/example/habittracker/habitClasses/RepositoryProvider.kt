package com.example.habittracker.habitClasses

object RepositoryProvider {
    fun provideRepository(): SearchRepository {
        return SearchRepository(HabitService.create())
    }
}
