package com.example.habittracker

import android.app.Application
import com.example.habittracker.dagger.ApplicationComponent
import com.example.habittracker.dagger.ContextModule
import com.example.habittracker.dagger.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent : ApplicationComponent
        private set


    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this)).build()


    }
}
