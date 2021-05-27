package com.example.data.web

import com.example.data.HabitMap
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitPriority
import com.example.domain.entities.HabitType
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class HabitTypeAdapter : TypeAdapter<HabitMap>() {

    companion object {
        private const val NAME = "title"
        private const val DESCRIPTION = "description"
        private const val PRIORITY = "priority"
        private const val TYPE = "type"
        private const val FREQUENCY = "frequency"
        private const val COUNT = "count"
        private const val COLOR = "color"
        private const val DATE = "date"
        const val UID = "uid"
        private const val DONE_DATES = "done_dates"

    }
    override fun write(out: JsonWriter?, value: HabitMap?) {
        out!!.beginObject()
        out.apply {
            name(NAME).value(value?.name)
            name(DESCRIPTION).value(value?.description ?: " ")
            name(PRIORITY).value(value?.priority!!.value ?: 0)
            name(TYPE).value(value.type.value)
            name(FREQUENCY).value(value?.period)
            name(COUNT).value(value?.times ?: 0)
            name(COLOR).value(value?.color ?: 0)
            name(DATE).value( value?.date)
            name(DONE_DATES).beginArray()
            value!!.doneDates.forEach{ out.value(it)}
            endArray()
        }
        if (value!!.uid != null)
            out.name(UID).value(value.uid)
        out.endObject()
    }



    override fun read(`in`: JsonReader?): HabitMap {
        var habitName = ""
        var description = ""
        var type = 0
        var priority = 0
        var frequency = 0
        var count = 0
        var date = 0
        var done_dates = mutableListOf<Int>()
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
                NAME -> habitName = `in`.nextString()
                DESCRIPTION -> description = `in`.nextString()
                PRIORITY -> priority = `in`.nextInt()
                TYPE -> type = `in`.nextInt()
                COUNT -> count = `in`.nextInt()
                COLOR -> color = `in`.nextInt()
                FREQUENCY -> frequency = `in`.nextInt()
                UID -> uid = `in`.nextString()
                DATE -> date = `in`.nextInt()
                DONE_DATES -> {`in`.beginArray()
                    while (`in`.peek() != JsonToken.END_ARRAY)
                        done_dates.add(`in`.nextInt())
                    `in`.endArray()}
            }
        }

        `in`?.endObject()

        val habitMap = HabitMap(habitName,description, HabitType.fromInt(type),
            HabitPriority.fromInt(priority), count,frequency,color)
        habitMap.uid = uid
        habitMap.date = date
        habitMap.doneDates = done_dates

        return habitMap
    }
}