package com.example.data.db

import androidx.room.*
import com.example.data.DatesConverter
import com.example.data.HabitMap
import com.example.data.PriorityConverter
import com.example.data.TypeConverter
import kotlinx.coroutines.flow.Flow

@Suppress("AndroidUnresolvedRoomSqlReference")
@Dao
@TypeConverters(TypeConverter::class, PriorityConverter::class, DatesConverter::class)
interface HabitDao {

    @Query("SELECT * FROM HabitMap")
    fun getAll() : Flow<List<HabitMap>>

    @Query("SELECT  * FROM HabitMap WHERE name LIKE :name")
    fun getByName(name: String): HabitMap?

    @Query("SELECT * FROM HabitMap WHERE uid LIKE :uid")
    fun getById(uid : String?): HabitMap?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: HabitMap) : Long

    @Delete
    fun delete(habit: HabitMap)

    @Update
    fun updateHabit(habit: HabitMap)
}