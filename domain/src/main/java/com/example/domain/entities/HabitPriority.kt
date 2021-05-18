package com.example.domain.entities

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
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }

}