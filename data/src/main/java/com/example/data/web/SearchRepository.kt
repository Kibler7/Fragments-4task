package com.example.data.web

import com.example.domain.entities.Habit
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call

class SearchRepository(private val apiService: HabitService) {

    companion object {
        const val token = "dfd9911d-be5c-4ce8-b46f-dce2a45147bd"
    }

    fun getHabits(): Call<ArrayList<Habit>> {
        return apiService.getHabitsList(token)
    }

    fun putHabit(habit: Habit): Call<LinkedTreeMap<String, String>> {
        return apiService.putHabit(token, habit)
    }

    fun deleteHabit(habit: Habit): Call<Void> {
        return apiService.deleteHabit(token,
            LinkedTreeMap<String, String>().also { it["uid"] = habit.uid })
    }
}