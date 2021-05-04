package com.example.habittracker.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.ui.fragments.HabitList.HabitListViewModel

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.habit_list_item.view.*

class HabitAdapter(
private val  viewModel: HabitListViewModel,
private val onItemClick: ((Habit) -> Unit),
private val context: Context?
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>(),
ITouchHelperAdapter {



    private var habits: List<Habit> = viewModel.getHabits() ?: emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(inflater.inflate(R.layout.habit_list_item, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }



    override fun deleteItem(position: Int) {
        viewModel.habitDeleted(habits[position])
        notifyItemRemoved(position)
    }

    fun refreshHabits(habitList: List<Habit>) {
        habits = habitList
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(habits[adapterPosition])
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(habit: Habit) {
            containerView.habit_name.text = habit.name

            val times = context!!.resources.getQuantityString(R.plurals.plurals_times, habit.times, habit.times)
            val days = when (habit.period) {
                1 -> context.getString(R.string.word_day_text)
                else -> context.resources.getQuantityString(R.plurals.plurals_days, habit.period, habit.period)
            }

            containerView.habit_description.text = "${context.getString(R.string.word_repeat)} $times ${context.getString(R.string.word_in)} $days"

            val stateList = ColorStateList.valueOf(habit.color)
            containerView.cardView.backgroundTintList = stateList
        }
    }
}