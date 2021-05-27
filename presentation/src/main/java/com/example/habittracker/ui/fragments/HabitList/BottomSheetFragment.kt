package com.example.habittracker.ui.fragments.HabitList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.R
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment() : Fragment() {

    private lateinit var viewModel : HabitListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment()).get(HabitListViewModel::class.java)
        setupSort()
        parentFragment?.view?.findViewById<View>(R.id.bottom_sheet_cont) ?: null


        habit_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupSort() {
        sort_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                viewModel.sortList(position)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }
}