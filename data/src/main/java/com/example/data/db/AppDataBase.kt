package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.entities.Habit

@Database(entities = [Habit::class], version = 1)
abstract class AppDataBase : RoomDatabase(){
    abstract fun habitDao(): HabitDao
}