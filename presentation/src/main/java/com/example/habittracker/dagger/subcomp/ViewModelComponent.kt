package com.example.habittracker.dagger.subcomp

import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment
import com.example.habittracker.ui.fragments.redactor.HabitRedactorViewModel
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class ViewModelScope

@ViewModelScope
@Subcomponent(modules = [HabitRedactorViewModelModule::class])
interface ViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: HabitRedactorViewModelModule?): Builder?
        fun build(): ViewModelComponent?
    }

    fun injectFragment2(habitRedactorFragment: HabitRedactorFragment)
}