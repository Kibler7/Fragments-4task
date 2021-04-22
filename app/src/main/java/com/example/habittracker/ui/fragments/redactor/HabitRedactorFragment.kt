package com.example.habittracker.ui.fragments.redactor

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.habitClasses.Habit
import com.example.habittracker.habitClasses.HabitPriority
import com.example.habittracker.habitClasses.HabitType
import kotlinx.android.synthetic.main.fragment_habit_redactor.*

class HabitRedactorFragment : Fragment(), ColorChoseDialog.OnInputListener {

    companion object {

        const val HABIT_KEY = "habit"
        const val ADD_HABIT_KEY = 3
        const val CHANGE_HABIT_KEY = 2
        const val REQUEST_CODE = "requestCode"

    }


    lateinit var viewModel : HabitRedactorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitRedactorViewModel() as T
            }
        }).get(HabitRedactorViewModel::class.java)
        return inflater.inflate(R.layout.fragment_habit_redactor, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.color.observe(viewLifecycleOwner, Observer {
            color_pick_fab.backgroundTintList = ColorStateList.valueOf(it)
        })

        when (arguments?.getInt(REQUEST_CODE)) {
            ADD_HABIT_KEY -> readyFab.setOnClickListener {
                closeKeyboard()
                saveNewData()
            }
            CHANGE_HABIT_KEY -> {
                changeTitle()
                val habit = requireArguments().getSerializable(HABIT_KEY)
                readyFab.setOnClickListener {
                    closeKeyboard()
                    saveChangedData(habit as Habit)
                }
                updateTextForRedact(habit as Habit)
            }
        }
        color_pick_fab.setOnClickListener {
            findNavController().navigate(R.id.action_habitRedactorFragment_to_colorChoseDialog)
        }
    }

    override fun sendColor(color: Int) {
        viewModel.changeColor(color)
    }

    private fun closeKeyboard(){
        activity?.currentFocus?.let { view ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun updateTextForRedact(habit: Habit) {

        edit_name.setText(habit.name)
        edit_description.setText(habit.description)
        spinner.setSelection(habit.priority.value)
        edit_frequency.setText(habit.period.toString())
        edit_times.setText(habit.times.toString())
        when (habit.type) {
            HabitType.GOOD -> radioGroup.check(R.id.firstRadio)
            HabitType.BAD -> radioGroup.check(R.id.secondRadio)
        }
        viewModel.changeColor(habit.color)
    }

    private fun validation(): Boolean {
        var allFieldsFilled = true

        if (edit_name.text.isEmpty()){
            allFieldsFilled = false
            edit_name_text_error.visibility = View.VISIBLE
        }
        else
            edit_name_text_error.visibility = View.INVISIBLE

        if (radioGroup.checkedRadioButtonId == -1){
            allFieldsFilled = false
            radio_group_error.visibility = View.VISIBLE
        }
        else
            radio_group_error.visibility = View.INVISIBLE
        if (edit_times.text.toString().isEmpty() ||
            edit_frequency.text.toString().isEmpty()){
            allFieldsFilled = false
            edit_frequency_error.visibility = View.VISIBLE
        }
        else
            edit_frequency_error.visibility = View.INVISIBLE
        if (!allFieldsFilled)
            errorText.visibility = View.VISIBLE

        return allFieldsFilled
    }


    private fun saveNewData() {
        if (validation()) {
            val habit = collectHabit()
            viewModel.addHabit(habit)
            findNavController().navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveChangedData(habit: Habit) {

        if (validation()) {
            val newHabit = collectHabit()
            newHabit.id = habit.id
            viewModel.updateHabit(newHabit, habit)
            findNavController().navigate(R.id.action_habitRedactorFragment_to_viewPagerFragment)
        }
    }

    private fun collectHabit(): Habit {
        val habit = Habit(
            -1,
            edit_name.text.toString(), edit_description.text.toString(),
            HabitType.fromInt(radioGroup.indexOfChild(requireView().findViewById(radioGroup.checkedRadioButtonId))),
            HabitPriority.fromInt(spinner.selectedItemPosition),
            Integer.valueOf(edit_times.text.toString()),
            Integer.valueOf(edit_frequency.text.toString()),
            viewModel.color.value!!)


        return habit
    }

    private fun changeTitle(){
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.change_habit_title)
    }


}