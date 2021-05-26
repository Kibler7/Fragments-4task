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

@Module
class HabitListViewModelModule (private val habitListFragment: HabitListFragment,
                                private val habitType: HabitType
) {

    @ViewModelScope
    @Provides
    fun provideHabitListViewModel(
        habitsUseCase: GetHabitsUseCase,
        deleteHabitUseCase: DeleteHabitUseCase,
        postHabitUseCase: PostHabitsUseCase
    ): HabitListViewModel {

        return ViewModelProvider(habitListFragment, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(
                    habitsUseCase,
                    deleteHabitUseCase,
                    postHabitUseCase,
                    habitType
                ) as T
            }
        }).get(HabitListViewModel::class.java)

    }
}