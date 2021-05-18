package com.example.domain.entities

enum class HabitType(val value: Int) {
    GOOD(0),
    BAD(1);


    override fun toString(): String {
        return if (this == BAD)
            "Вредная"
        else
            "Полезная"
    }

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}