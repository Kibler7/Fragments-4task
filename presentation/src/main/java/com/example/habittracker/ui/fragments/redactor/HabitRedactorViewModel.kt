package com.example.habittracker.ui.fragments.redactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitPriority
import com.example.domain.entities.HabitType
import com.example.domain.useCases.AddHabitUseCase
import com.example.domain.useCases.UpdateHabitUseCase
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HabitRedactorViewModel(private val addHabitUseCase: AddHabitUseCase,
                             private val updateHabitUseCase: UpdateHabitUseCase): ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler{_, e -> throw e}


    private var _color = MutableLiveData<Int>().apply {
        value = MainActivity.CONTEXT.resources.getColor(R.color.colorGreen)
    }

    val color : LiveData<Int> = _color
    var name = MutableLiveData<String>().apply {   value = ""  }
    var desription = MutableLiveData<String>().apply {    value = "" }
    var type = MutableLiveData<HabitType>().apply { value = null }
    var priority = MutableLiveData<HabitPriority>().apply { value = HabitPriority.HIGH }
    private var doneDates = mutableListOf<Int>()

    val frequencyText : MutableLiveData<String> = MutableLiveData<String>()
    var frequency = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            frequencyText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }

    val timesText : MutableLiveData<String> = MutableLiveData<String>()
    var times = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            timesText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }


    fun getPriority(position : Int){
        when (position){
            0 -> priority.value = HabitPriority.HIGH
            1 -> priority.value = HabitPriority.MEDIUM
            2 -> priority.value = HabitPriority.LOW
        }
    }

    var isEnteredName = MutableLiveData<Boolean>().apply { value = true }
    var isSelectedType = MutableLiveData<Boolean>().apply { value = true }
    var isEnteredFrequency = MutableLiveData<Boolean>().apply { value = true }
    var isNeededErrorText = MutableLiveData<Boolean>().apply { value = false }

    fun updateHabitData(habit: Habit) {
        name.value = habit.name
        desription.value = habit.description.dropLast(1)
        priority.value = habit.priority
        frequency.value = habit.period
        times.value = habit.times
        type.value = habit.type
        changeColor(habit.color)
        doneDates = habit.doneDates
    }


    fun validation(): Boolean {
        var allFieldsFilled = true

        if (name.value!!.isEmpty()){
            allFieldsFilled = false
            isEnteredName.value = false
        }
        else
            isEnteredName.value = true

        if (type.value == null){
            allFieldsFilled = false
            isSelectedType.value = false
        }
        else
            isSelectedType.value = true
        if (times.value == null ||
                frequency.value == null){
            allFieldsFilled = false
            isEnteredFrequency.value = false
        }
        else
            isEnteredFrequency.value = true

        isNeededErrorText.value = !allFieldsFilled

        return allFieldsFilled
    }

    private fun collectHabit(): Habit {
        val habit = Habit(
            name.value!!, desription.value!! + " ", type.value!!,
            priority.value!!, times.value!!, frequency.value!!, color.value!!
        )
        habit.doneDates = doneDates
        return habit
    }

    fun changeColor(newColor : Int) {
        _color.value = newColor
    }


    fun saveNewHabit() = launch {
        withContext(Dispatchers.IO) {
            val habit = collectHabit()
            addHabitUseCase.addHabit(habit)
        }
    }


    fun saveChangedHabit(habit: Habit) = launch {
        withContext(Dispatchers.IO) {
            val newHabit = collectHabit()
            newHabit.id = habit.id
            newHabit.date = habit.date
            if (habit.uid != null)
                newHabit.uid = habit.uid
            updateHabitUseCase.updateHabit(newHabit)
        }
    }
}