package com.example.habittracker.ui.fragments.HabitList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.NewItemTouchHelper
import com.example.habittracker.habitClasses.HabitType
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_habit_list.*


class HabitListFragment : Fragment(), LifecycleOwner {

    companion object {
        const val HABIT_TYPE = "habit_type"
        fun newInstance(habitType: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: HabitListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val habitType = this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(habitType as HabitType, findNavController()) as T
            }
        }).get(HabitListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener {
            closeKeyBoard()
            viewModel.createNewHabit() }
        addAdapter()
        observeViewModels()
        addBottomSheet()
    }

    private fun addBottomSheet(){
        val bottomSheet = BottomSheetFragment()
        childFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_cont, bottomSheet)
                .commit()
    }

    private fun observeViewModels() {
        viewModel.habits.observe(viewLifecycleOwner, Observer {
            it.let {
                (habit_list.adapter as HabitAdapter).refreshHabits(
                        it
                )
            }
        })
    }



    private fun addAdapter() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HabitAdapter(viewModel, {
                    habit ->
                closeKeyBoard()
                viewModel.changeHabit(habit) },
                            this@HabitListFragment.context)
        }
        habit_list.adapter!!.notifyDataSetChanged()
        val habitAdapter = habit_list.adapter as HabitAdapter
        val callback: ItemTouchHelper.Callback = NewItemTouchHelper(habitAdapter)
        val myItemTouchHelper = ItemTouchHelper(callback)
        myItemTouchHelper.attachToRecyclerView(habit_list)
    }



    private fun closeKeyBoard(){
        activity?.currentFocus?.let { view ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}