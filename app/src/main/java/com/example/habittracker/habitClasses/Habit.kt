package com.example.habittracker.habitClasses

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.graphics.blue
import com.example.habittracker.R
import java.io.FileDescriptor
import java.io.Serializable

data class Habit(var id: Long, val name: String, val description: String, val type: HabitType,
                 val priority: HabitPriority, val time: Int, val period: Int, var color : Int)
    : Serializable {

    var stringFrequency = ""
}