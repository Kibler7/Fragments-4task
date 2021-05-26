package com.example.domain.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable
@Entity
@TypeConverters(com.example.domain.entities.TypeConverter::class, PriorityConverter::class, DatesConverter::class)
public data class Habit(@SerializedName("token")val name:
                 String, val description: String, val type: HabitType,
                 val priority: HabitPriority, @SerializedName("count") val times: Int,
                 val period: Int, var color : Int)
    : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var uid: String? = null
    var date = 0
    var doneDates = mutableListOf<Int>()

    fun post(date : Int){
        doneDates.add(date)
    }

    fun getCountDone(today : Int) : Int{
        val lastUpdateDay = today - today.rem(this.period)
        return this.doneDates.filter { it >= lastUpdateDay }.size
    }
}
class DatesConverter {
    @TypeConverter
    fun fromDates(doneDates: MutableList<Int>): String {
        val gson = Gson()
        return gson.toJson(doneDates)
    }

    @TypeConverter
    fun toDates(datesString: String): MutableList<Int> {
        val listType = object : TypeToken<MutableList<Int>>() {}.type
        return Gson().fromJson(datesString, listType)
    }
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
