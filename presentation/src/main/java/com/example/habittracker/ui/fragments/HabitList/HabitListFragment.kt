package com.example.habittracker.ui.fragments.HabitList

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.entities.Habit
import com.example.domain.entities.HabitType
import com.example.habittracker.App
import com.example.habittracker.R
import com.example.habittracker.adapters.HabitAdapter
import com.example.habittracker.adapters.NewItemTouchHelper
import com.example.habittracker.ui.fragments.redactor.HabitRedactorFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_habit_list.*
import java.util.*
import javax.inject.Inject


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

    @Inject
    lateinit var viewModel: HabitListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as App).createViewModelHabitListComponent(this,
            this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE) as HabitType)
        (requireActivity().application as App).listViewModelComponent.injectHabitListFragment(this)
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener {
            closeKeyBoard()
            createNewHabit() }
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
        viewModel.habitsData.observe(viewLifecycleOwner, Observer {
            it.let {
                (habit_list.adapter as HabitAdapter).refreshHabits(it)
            }
        })
    }



    private fun addAdapter() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HabitAdapter(viewModel, {
                    habit ->closeKeyBoard()
                changeHabit(habit) }, this@HabitListFragment.context, {
                        post(it)})
        }
        habit_list.adapter!!.notifyDataSetChanged()
        val habitAdapter = habit_list.adapter as HabitAdapter
        val callback: ItemTouchHelper.Callback = NewItemTouchHelper(habitAdapter)
        val myItemTouchHelper = ItemTouchHelper(callback)
        myItemTouchHelper.attachToRecyclerView(habit_list)
    }

    private fun post(habit: Habit){
        viewModel.postHabit(habit)
        val timesLeft = habit.times - habit.getCountDone(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) - 1
        val text = if (habit.type == HabitType.GOOD){
            when (timesLeft) {
                in 0..Int.MAX_VALUE -> "${getString(R.string.good_toast1)} ${
                    resources.getQuantityString(
                        R.plurals.plurals_times, timesLeft, timesLeft
                    )
                }"
                else -> getString(R.string.good_toast2)
            }
        } else{
            when (timesLeft) {
                in 0..Int.MAX_VALUE-> "${getString(R.string.bad_toast1)} ${
                    resources.getQuantityString(
                        R.plurals.plurals_times, timesLeft, timesLeft
                    )
                }"
                else -> getString(R.string.bad_toast2)
            }
        }
        val toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 30)
        toast.show()
    }



    private fun closeKeyBoard(){
        activity?.currentFocus?.let { view ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun createNewHabit() {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.ADD_HABIT_KEY)
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }

    private fun changeHabit(habit: Habit) {
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.REQUEST_CODE, HabitRedactorFragment.CHANGE_HABIT_KEY)
        bundle.putSerializable(HabitRedactorFragment.HABIT_KEY, habit)
        findNavController().navigate(R.id.action_viewPagerFragment_to_habitRedactorFragment, bundle)
    }
}