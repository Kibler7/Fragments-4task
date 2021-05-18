package com.example.data.web

import com.example.domain.entities.Habit
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface HabitService {

    @GET("api/habit")
    fun getHabitsList(@Header("Authorization") token: String): Call<ArrayList<Habit>>

    @PUT("api/habit")
    fun putHabit(@Header("Authorization") token: String, @Body habit: Habit) : Call<LinkedTreeMap<String, String>>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    fun deleteHabit(@Header("Authorization") token: String, @Body uid : LinkedTreeMap<String, String>) : Call<Void>

    companion object Factory {

        const val URL = "https://droid-test-server.doubletapp.ru/"

        fun create() : HabitService {
            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor {
                    val request = it.request()
                    var response = it.proceed(request)
                    if (!response.isSuccessful){
                        Thread.sleep(500)
                        response = it.proceed(request)
                    }
                    response
                }.build()

            val gSon = GsonBuilder().registerTypeAdapter(Habit::class.java, HabitTypeAdapter()).create()
            val retrofit = Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .baseUrl(URL).build()


            return retrofit.create(HabitService::class.java)

        }


    }
}