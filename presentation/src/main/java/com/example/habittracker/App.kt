package com.example.habittracker

import android.app.Application
import com.example.domain.entities.HabitType
import com.example.habittracker.dagger.ApplicationComponent
import com.example.habittracker.dagger.ContextModule
import com.example.habittracker.dagger.DaggerApplicationComponent
import com.example.habittracker.dagger.subcomp.HabitListViewModelComponent
import com.example.habittracker.dagger.subcomp.HabitListViewModelModule
import com.example.habittracker.dagger.subcomp.HabitRedactorViewModelModule
import com.example.habittracker.dagger.subcomp.ViewModelComponent
import com.example.habittracker.ui.fragments.HabitList.HabitListFragment
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment
import com.example.habittracker.ui.fragments.redactor.HabitRedactorViewModel

class App : Application() {

    lateinit var applicationComponent : ApplicationComponent
        private set

    lateinit var viewModelComponent: ViewModelComponent

    lateinit var listViewModelComponent: HabitListViewModelComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this)).build()
    }

    fun createViewModelHabitListComponent(habitListFragment: HabitListFragment, habitType: HabitType){
        listViewModelComponent = applicationComponent.
            getListViewModelComponent().requestModule(
            HabitListViewModelModule(habitListFragment,habitType)
        )!!.build()!!
    }

    fun createViewModelRedactorComponent(habitRedactorFragment: HabitRedactorFragment){
        viewModelComponent = applicationComponent.getViewModelComponent().requestModule(
            HabitRedactorViewModelModule(habitRedactorFragment))!!.build()!!
    }
}
