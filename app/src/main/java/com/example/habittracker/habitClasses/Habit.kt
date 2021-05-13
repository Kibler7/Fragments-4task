package com.example.habittracker.habitClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Entity
@TypeConverters(com.example.habittracker.habitClasses.TypeConverter::class, PriorityConverter::class)
data class Habit(@SerializedName("token")val name:
                 String, val description: String, val type: HabitType,
                 val priority: HabitPriority, @SerializedName("count") val times: Int,
                 val period: Int, var color : Int)
    : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var uid: String? = null
    var date = 0
    }

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
