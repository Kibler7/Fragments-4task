package com.example.habittracker.dagger.subcomp

import com.example.habittracker.ui.fragments.HabitList.HabitListFragment
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(modules = [HabitListViewModelModule::class])
interface HabitListViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun getModule(module: HabitListViewModelModule?): Builder?
        fun build(): HabitListViewModelComponent?
    }

    fun injectHabitListFragment(habitListFragment: HabitListFragment)
}