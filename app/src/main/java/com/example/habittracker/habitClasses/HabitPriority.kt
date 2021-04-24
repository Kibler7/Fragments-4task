package com.example.habittracker.habitClasses

enum class HabitPriority(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2);

    override fun toString(): String{
        return when (this){
            HIGH -> "Высокий"
            MEDIUM -> "Средний"
            else -> "Низкий"
        }
    }

}