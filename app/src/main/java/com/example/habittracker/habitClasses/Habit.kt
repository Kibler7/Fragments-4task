package com.example.habittracker.habitClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.Serializable
@Entity
@TypeConverters(com.example.habittracker.habitClasses.TypeConverter::class, PriorityConverter::class)
data class Habit(@PrimaryKey var id: Long, val name: String, val description: String,val type: HabitType,
                 val priority: HabitPriority,  val times: Int, val period: Int, var color : Int)
    : Serializable

class TypeConverter {
    @TypeConverter
    fun fromType(type: HabitType): String {
        return type.toString()
    }

    @TypeConverter
    fun toType(data: String): HabitType {
        return when (data) {
            "Вредная" -> HabitType.BAD
            else -> HabitType.GOOD
        }
    }
}

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: HabitPriority): String {
        return priority.toString()
    }

    @TypeConverter
    fun toPriority(data: String): HabitPriority {
        return when (data) {
            "Высокий" -> HabitPriority.HIGH
            "Средний" -> HabitPriority.MEDIUM
            else -> HabitPriority.LOW
        }
    }
}
