package com.example.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.entities.Habit
import com.example.domain.entities.PriorityConverter

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
@TypeConverters(TypeConverter::class, PriorityConverter::class)
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getAll() : LiveData<List<Habit>>

    @Query("SELECT  * FROM Habit WHERE name LIKE :name")
    fun getByName(name: String): Habit?

    @Query("SELECT * FROM Habit WHERE uid LIKE :uid")
    fun getById(uid : String?): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: Habit) : Long



    @Delete
    fun delete(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)
}