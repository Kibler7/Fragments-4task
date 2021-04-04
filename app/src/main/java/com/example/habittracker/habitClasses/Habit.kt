package com.example.habittracker.habitClasses

import java.io.Serializable

data class Habit(var id: Long, val name: String, val description: String, val type: HabitType,
                 val priority: HabitPriority, val time: Int, val period: Int, var color : Int)
    : Serializable {

    var stringFrequency = ""
}