package com.example.data.web

import com.example.domain.entities.Habit
import com.example.domain.entities.HabitPriority
import com.example.domain.entities.HabitType
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class HabitTypeAdapter : TypeAdapter<Habit>() {
    override fun write(out: JsonWriter?, value: Habit?) {
        out!!.beginObject()
        out.apply {
            name("title").value(value?.name)
            name("description").value(value?.description ?: " ")
            name("priority").value(value?.priority!!.value ?: 0)
            name("type").value(value.type.value)
            name("frequency").value(value?.period)
            name("count").value(value?.times ?: 0)
            name("color").value(value?.color ?: 0)
            name("date").value( value?.date)
        }
        if (value!!.uid != null)
            out.name("uid").value(value.uid)
        out.endObject()
    }

    // разбить в 2 модеали


    override fun read(`in`: JsonReader?): Habit {
        var habitName = ""
        var description = ""
        var type = 0
        var priority = 0
        var frequency = 0
        var count = 0
        var color = 0
        var uid = ""

        `in`?.beginObject()

        while (`in`?.hasNext() == true) {
            val name = `in`.nextName()
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                continue
            }

            when (name) {
                "title" -> habitName = `in`.nextString()
                "description" -> description = `in`.nextString()
                "priority" -> priority = `in`.nextInt()
                "type" -> type = `in`.nextInt()
                "count" -> count = `in`.nextInt()
                "color" -> color = `in`.nextInt()
                "frequency" -> frequency = `in`.nextInt()
                "uid" -> uid = `in`.nextString()
                "date" -> `in`.nextInt()
                "done_dates" -> {`in`.beginArray()
                    `in`.endArray()}
            }
        }

        `in`?.endObject()

        val habit = Habit(habitName,description, HabitType.fromInt(type),
            HabitPriority.fromInt(priority), count,frequency,color)
        habit.uid = uid

        return habit
    }
}