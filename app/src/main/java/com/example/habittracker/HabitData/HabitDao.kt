package com.example.habittracker.HabitData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitType
import com.example.habittracker.habitClasses.PriorityConverter

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
@TypeConverters(com.example.habittracker.habitClasses.TypeConverter::class, PriorityConverter::class)
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAll() : LiveData<List<Habit>>

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getById(id : Long) : LiveData<Habit>

    @Query("SELECT * FROM habit WHERE type = :type")
    fun getHabitsByType(type : HabitType ) : List<Habit>

    @Insert
    fun insert(habit: Habit)

    @Delete
    fun delete(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)
}