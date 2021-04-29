package com.example.habittracker.ui.fragments.redactor

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitRedactorBinding
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
                return HabitRedactorViewModel(findNavController()) as T
            }
        }).get(HabitRedactorViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentHabitRedactorBinding>(inflater,
            R.layout.fragment_habit_redactor, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (arguments?.getInt(REQUEST_CODE)) {
            ADD_HABIT_KEY -> readyFab.setOnClickListener {
                closeKeyboard()
                viewModel.saveNewHabit()
            }
            CHANGE_HABIT_KEY -> {
                changeTitle()
                val habit = requireArguments().getSerializable(HABIT_KEY)
                readyFab.setOnClickListener {
                    closeKeyboard()
                    viewModel.saveChangedHabit(habit as Habit)
                }
                viewModel.updateHabitData(habit as Habit)
            }
        }
        sendToViewModel()
        observeViewModel()
        color_pick_fab.setOnClickListener {
            findNavController().navigate(R.id.action_habitRedactorFragment_to_colorChoseDialog)
        }
    }

    override fun sendColor(color: Int) {
        viewModel.changeColor(color)
    }

    private fun sendToViewModel(){
        edit_name.doOnTextChanged { text, _, _, _ ->
            viewModel.name.value = text.toString()
        }
        edit_description.doOnTextChanged { text, _, _, _ ->
            viewModel.desription.value = text.toString()
        }

        firstRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.type.value = HabitType.GOOD
        }
        secondRadio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.type.value = HabitType.BAD
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.getPriority(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        edit_frequency.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty())
                viewModel.frequency.value = text.toString().toInt()
            else
                viewModel.frequency.value = null
        }
        edit_times.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty())
                viewModel.times.value = text.toString().toInt()
            else
                viewModel.timesText.value = null
        }
    }

    private fun observeViewModel(){
        viewModel.color.observe(viewLifecycleOwner, Observer {
            color_pick_fab.backgroundTintList = ColorStateList.valueOf(it)
        })
        spinner.setSelection(viewModel.priority.value!!.value)
    }

    private fun closeKeyboard(){
        activity?.currentFocus?.let { view ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun changeTitle(){
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.change_habit_title)
    }
}