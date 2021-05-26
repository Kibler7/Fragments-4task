package com.example.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.data.db.AppDataBase
import com.example.data.web.SearchRepository
import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class HabitRepositoryImpl(private val dataBase: AppDataBase,
                          private val retrofitService: SearchRepository) : HabitRepository {

    private var localHabits = dataBase.habitDao().getAll()
    private var remoteHabits : List<Habit>? = null

    init {
        getDataFromServer()
    }


    override fun addHabit(habit: Habit) {
        val newId = dataBase.habitDao().insert(habit)
        habit.id = newId
        sendToServer(habit)
    }


    override fun deleteHabit(habit: Habit) {
        dataBase.habitDao().delete(habit)
        deleteFromServer(habit)
    }

    override fun updateHabit(habit: Habit) {
        habit.date++
        dataBase.habitDao().updateHabit(habit)
        sendToServer(habit)
    }

    override fun getLocalData(): LiveData<List<Habit>> =  localHabits
    override fun postHabit(habit: Habit, date: Int) = GlobalScope.launch {
        withContext(Dispatchers.IO){
            dataBase.habitDao().updateHabit(habit)
            retrofitService.postHabit(habit, date).awaitResponse()

        }
    }


    private fun  getDataFromServer() = GlobalScope.launch {
        withContext(Dispatchers.IO){
            remoteHabits = retrofitService.getHabits().awaitResponse().body()
            insertFromServerToDB(remoteHabits)
        }
    }


    private fun sendToServer(habit: Habit) =
        GlobalScope.launch(Dispatchers.IO) {
            val response = retrofitService.putHabit(habit).awaitResponse()
            if (response.isSuccessful){
                habit.uid = response.body()!!["uid"]
                dataBase.habitDao().updateHabit(habit)
            }
        }

    private fun deleteFromServer(habit: Habit) =
        GlobalScope.launch(Dispatchers.IO) {
            if (habit.uid != null) {
                retrofitService.deleteHabit(habit).awaitResponse()
            }
        }

    private fun insertFromServerToDB(habits: List<Habit>?) {
        habits?.forEach {
            val habit = dataBase.habitDao().getById(it.uid)
            if (habit == null)
                dataBase.habitDao().insert(it)

        }
    }



}