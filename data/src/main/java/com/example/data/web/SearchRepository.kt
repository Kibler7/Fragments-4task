package com.example.data.web

import com.example.data.HabitMap
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call

class SearchRepository(private val apiService: HabitService) {

    companion object {
        const val token = "dfd9911d-be5c-4ce8-b46f-dce2a45147bd"
    }

    fun getHabits(): Call<ArrayList<HabitMap>> {
        return apiService.getHabitsList(token)
    }

    fun putHabit(habit: HabitMap): Call<LinkedTreeMap<String, String>> {
        return apiService.putHabit(token, habit)
    }

    fun deleteHabit(habit: HabitMap): Call<Void> {
        return apiService.deleteHabit(token,
            LinkedTreeMap<String, String>().also { it["uid"] = habit.uid })
    }

    fun postHabit(habit: HabitMap, date : Int): Call<Void>{
        return apiService.postHabit(token, LinkedTreeMap<String, Any>().also {
            it["date"] = date
            it["habit_uid"] = habit.uid
        })
    }
}