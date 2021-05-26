package com.example.habittracker.dagger.subcomp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entities.HabitType
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.PostHabitsUseCase
import com.example.habittracker.ui.fragments.HabitList.HabitListFragment
import com.example.habittracker.ui.fragments.HabitList.HabitListViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(modules = [HabitListViewModelModule::class])
interface HabitListViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: HabitListViewModelModule?): Builder?
        fun build(): HabitListViewModelComponent?
    }

    fun injectFragment(habitListFragment: HabitListFragment)
}