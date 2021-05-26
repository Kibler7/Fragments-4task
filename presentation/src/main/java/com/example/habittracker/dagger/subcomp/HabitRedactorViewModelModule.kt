package com.example.habittracker.dagger.subcomp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.useCases.AddHabitUseCase
import com.example.domain.useCases.UpdateHabitUseCase
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment
import com.example.habittracker.ui.fragments.redactor.HabitRedactorViewModel
import dagger.Module
import dagger.Provides


@Module
class HabitRedactorViewModelModule (private val fragment: HabitRedactorFragment) {

    @ViewModelScope
    @Provides
    fun provideRedactorViewModel(
        addHabitUseCase: AddHabitUseCase,
        updateHabitUseCase: UpdateHabitUseCase
    ): HabitRedactorViewModel {

        return ViewModelProvider(fragment, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitRedactorViewModel(
                    addHabitUseCase,
                    updateHabitUseCase
                ) as T
            }
        }).get(HabitRedactorViewModel::class.java)
    }
}