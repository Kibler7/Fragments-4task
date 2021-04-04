package com.example.habittracker.habitClasses

enum class HabitPriority(val value: Int) {
    HIGH(0),
    MEDIUM(2),
    LOW(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}