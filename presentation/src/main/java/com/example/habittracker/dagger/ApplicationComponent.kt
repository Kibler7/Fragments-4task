package com.example.habittracker.dagger

import com.example.domain.useCases.*
import com.example.habittracker.dagger.subcomp.HabitListViewModelComponent
import com.example.habittracker.dagger.subcomp.ViewModelComponent
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [HabitsModule::class, ContextModule::class])
interface ApplicationComponent {

    fun getAddHabitUseCase() : AddHabitUseCase
    fun getUpdateHabitUseCase() : UpdateHabitUseCase
    fun getDeleteHabitUseCase() : DeleteHabitUseCase
    fun getPostHabitUseCase() : PostHabitsUseCase

    fun getGetHabitsUseCase() : GetHabitsUseCase
    fun getViewModelComponent(): ViewModelComponent.Builder
    fun getListViewModelComponent(): HabitListViewModelComponent.Builder

}