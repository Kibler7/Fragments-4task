package com.example.habittracker.HabitData

import com.example.habittracker.App
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.RepositoryProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


class HabitRepository{

    companion object {
        val localHabits = App.database.habitDao().getAll()
        var remoteHabits: List<Habit>? = null
    }

    init {
        getRemoteHabits()
    }

    fun addHabit(habit : Habit){
        val newId = App.database.habitDao().insert(habit)
        habit.id = newId
        sendHabit(habit)
    }

    fun deleteHabit(habit: Habit){
        App.database.habitDao().delete(habit)
        deleteHabitFromServer(habit)
    }

    fun updateHabit(habit: Habit){
        habit.date++
        App.database.habitDao().updateHabit(habit)
        sendHabit(habit)
    }

    private fun getRemoteHabits() = GlobalScope.launch(Dispatchers.IO) {
        val response = RepositoryProvider.provideRepository()
            .getHabits().awaitResponse()
        remoteHabits = response.body()
        insertFromServerToDB(remoteHabits)
    }

    private fun insertFromServerToDB(habits: List<Habit>?) {
        habits?.forEach {
            val habit = App.database.habitDao().getById(it.uid)
            if (habit == null)
                App.database.habitDao().insert(it)

        }
    }

    private fun deleteHabitFromServer(habit: Habit) =
        GlobalScope.launch(Dispatchers.IO) {
            if (habit.uid != null) {
                RepositoryProvider.provideRepository()
                        .deleteHabit(habit).awaitResponse()
            }
        }

    private fun sendHabit(habit: Habit) =
        GlobalScope.launch(Dispatchers.IO) {
            val response = RepositoryProvider.provideRepository()
                    .putHabit(habit).awaitResponse()
            if (response.isSuccessful){
                habit.uid = response.body()!!["uid"]
                App.database.habitDao().updateHabit(habit)
            }
        }

}