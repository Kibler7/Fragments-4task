package com.example.habittracker.habitClasses

enum class HabitType(val value: Int) {
    GOOD(0),
    BAD(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }

    override fun toString(): String {
        return if (this == BAD)
            "Вредная"
        else
            "Полезная"
    }
}