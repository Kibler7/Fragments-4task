package com.example.data.web

import com.example.data.HabitMap
import com.example.domain.entities.Habit
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.http.*

interface HabitService {

    @GET("api/habit")
    fun getHabitsList(@Header("Authorization") token: String): Call<ArrayList<HabitMap>>

    @PUT("api/habit")
    fun putHabit(@Header("Authorization") token: String, @Body habit: HabitMap) : Call<LinkedTreeMap<String, String>>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    fun deleteHabit(@Header("Authorization") token: String, @Body uid : LinkedTreeMap<String, String>) : Call<Void>

    @POST("api/habit_done")
    fun postHabit(@Header("Authorization") token: String, @Body uidAndDate: LinkedTreeMap<String, Any>) : Call<Void>
}