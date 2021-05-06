package com.example.habittracker

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.example.habittracker.HabitData.AppDataBase

class App : Application() {

    companion object {
        lateinit var database: AppDataBase
    }
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
        AppDataBase::class.java, "database")
            .build()
    }
}
