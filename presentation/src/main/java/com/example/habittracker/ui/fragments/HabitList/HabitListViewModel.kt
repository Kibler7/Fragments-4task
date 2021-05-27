package com.example.habittracker.ui.fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitType
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.PostHabitsUseCase
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


class HabitListViewModel(private val getHabitsUseCase: GetHabitsUseCase,
                         private val deleteHabitUseCase: DeleteHabitUseCase,
                         private val postHabitsUseCase: PostHabitsUseCase,
                         private val habitType: HabitType) : ViewModel(), Filterable, CoroutineScope {


    private val mutableHabitsData = MutableLiveData<List<Habit>>()
    val habitsData: LiveData<List<Habit>> = mutableHabitsData
    private var notFilteredList = mutableHabitsData.value


    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler{_, e -> throw e}


    init {
        onCreate()
    }

    private lateinit var observer: Observer<List<Habit>>
    private fun onCreate() {
        observer = Observer {
            mutableHabitsData.value = it.filter {  it.type == habitType }
            notFilteredList = mutableHabitsData.value
        }
        getHabitsUseCase.getHabit().asLiveData().observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        getHabitsUseCase.getHabit().asLiveData().removeObserver(observer)
        coroutineContext.cancelChildren()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()
                val searchResult = FilterResults()
                if (searchString.isEmpty())
                    searchResult.values = notFilteredList
                else
                    searchResult.values = mutableHabitsData.value!!.filter { it.name.contains(searchString) }
                return searchResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabitsData.value = results?.values as? List<Habit>?
            }
        }
    }

    fun getHabits() = habitsData.value


    fun postHabit(habit: Habit)= launch{
        withContext(Dispatchers.IO) {
            postHabitsUseCase.postHabit(habit, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
        }
    }

    fun habitDeleted(habit: Habit) = launch {
        withContext(Dispatchers.IO) {
            deleteHabitUseCase.deleteHabit(habit)
        }
    }

    fun sortList(position: Int) = launch {
        withContext(Dispatchers.Default) {
            when (position) {
                0 -> mutableHabitsData.postValue(mutableHabitsData.value!!.sortedBy { it.id })
                1 -> mutableHabitsData.postValue(mutableHabitsData.value!!.sortedBy { it.name })
                2 -> mutableHabitsData.postValue(mutableHabitsData.value!!.sortedBy { it.priority.value })
            }
        }
    }
}
