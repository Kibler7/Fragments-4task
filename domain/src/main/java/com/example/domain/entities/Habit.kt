package com.example.domain.entities
import java.io.Serializable
data class Habit(val name: String, val description: String, val type: HabitType,
                 val priority: HabitPriority,  val times: Int,
                 val period: Int, var color : Int)
    : Serializable{
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

