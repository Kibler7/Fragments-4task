package com.example.data

import android.util.Log
import com.example.data.db.AppDataBase
import com.example.data.web.HabitTypeAdapter
import com.example.data.web.SearchRepository
import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.net.UnknownHostException

class HabitRepositoryImpl(private val dataBase: AppDataBase,
                          private val retrofitService: SearchRepository) : HabitRepository {

    private var localHabits = dataBase.habitDao().getAll()
    private var remoteHabits : List<HabitMap>? = null

    init {
        getDataFromServer()
    }


    override fun addHabit(habit: Habit) {
        val habitMap = HabitMap.toMap(habit)
        val newId = dataBase.habitDao().insert(habitMap)
        habitMap.id = newId
        sendToServer(habitMap)
    }


    override fun deleteHabit(habit: Habit) {
        val habitMap = HabitMap.toMap(habit)
        dataBase.habitDao().delete(habitMap)
        deleteFromServer(habitMap)
    }

    override fun updateHabit(habit: Habit) {
        habit.date++
        val habitMap = HabitMap.toMap(habit)
        dataBase.habitDao().updateHabit(habitMap)
        sendToServer(habitMap)
    }

    override fun getHabitsData(): Flow<List<Habit>> =  localHabits.map { it.map { HabitMap.toHabit(it) } }
    override fun postHabit(habit: Habit, date: Int) = GlobalScope.launch {
        withContext(Dispatchers.IO){
            val habitMap = HabitMap.toMap(habit)
            dataBase.habitDao().updateHabit(habitMap)
            try {
                retrofitService.postHabit(habitMap, date).awaitResponse()
            }
            catch (e : UnknownHostException){
                Log.e("e", "no connect")
            }
        }
    }


    private fun  getDataFromServer() = GlobalScope.launch {
        withContext(Dispatchers.IO){
            try {
                remoteHabits = retrofitService.getHabits().awaitResponse().body()
                insertFromServerToDB(remoteHabits)
            }
            catch (e : UnknownHostException){
                Log.e("e", "no connect")
            }
        }
    }


    private fun sendToServer(habit: HabitMap) =
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = retrofitService.putHabit(habit).awaitResponse()
                if (response.isSuccessful) {
                    habit.uid = response.body()!![HabitTypeAdapter.UID]
                    dataBase.habitDao().updateHabit(habit)
                }
            }
            catch (e : UnknownHostException){
                Log.e("e", "no connect")
            }
        }

    private fun deleteFromServer(habit: HabitMap) =
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (habit.uid != null) {
                    retrofitService.deleteHabit(habit).awaitResponse()
                }
            }
            catch (e : UnknownHostException){
                Log.e("e", "no connect")
            }
        }

    private fun insertFromServerToDB(habits: List<HabitMap>?) {
        habits?.forEach {
            val habit = dataBase.habitDao().getByName(it.name)
            if (habit == null)
                dataBase.habitDao().insert(it)
        }
    }



}