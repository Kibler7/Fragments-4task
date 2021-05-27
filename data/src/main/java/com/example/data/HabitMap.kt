package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitPriority
import com.example.domain.entities.HabitType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.example.data.DatesConverter
import com.example.data.PriorityConverter
import java.io.Serializable
@Entity
@TypeConverters(com.example.data.TypeConverter::class,
                PriorityConverter::class, DatesConverter::class)
data class HabitMap(@SerializedName("token")val name:
                    String, val description: String, val type: HabitType,
                    val priority: HabitPriority, @SerializedName("count") val times: Int,
                    val period: Int, var color : Int)
    : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var uid: String? = null
    var date = 0
    var doneDates = mutableListOf<Int>()

    companion object {
        fun toMap(habit: Habit): HabitMap {
            val habitMap = HabitMap(
                habit.name, habit.description, habit.type,
                habit.priority, habit.times, habit.period, habit.color
            )
            if (habit.id != null)
                habitMap.id = habit.id
            if (habit.uid != null)
                habitMap.uid = habit.uid
            habitMap.date = habit.date
            habitMap.doneDates = habit.doneDates
            return habitMap;
        }

        fun toHabit(habitMap: HabitMap): Habit {
            val habit = Habit(
                habitMap.name, habitMap.description, habitMap.type,
                habitMap.priority, habitMap.times, habitMap.period, habitMap.color
            )
            habit.date = habitMap.date
            habit.uid = habitMap.uid
            habit.id = habitMap.id
            habit.doneDates = habitMap.doneDates
            return habit
        }
    }
}
class DatesConverter {
    @TypeConverter
    fun fromDates(doneDates: MutableList<Int>): String = Gson().toJson(doneDates)


    @TypeConverter
    fun toDates(datesString: String): MutableList<Int> {
        val listType = object : TypeToken<MutableList<Int>>() {}.type
        return Gson().fromJson(datesString, listType)
    }
}

class TypeConverter {
    @TypeConverter
    fun fromType(type: HabitType): String = type.toString()

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
