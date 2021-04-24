package com.example.habittracker.HabitData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habittracker.habitClasses.Habit

@Database(entities = [Habit::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun habitDao(): HabitDao
}