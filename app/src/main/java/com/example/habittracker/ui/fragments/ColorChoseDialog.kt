package com.example.habittracker.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.example.habittracker.R

class ColorChoseDialog : DialogFragment(){

    lateinit var buttons: ArrayList<View>;
    var onInputListener: OnInputListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_color_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = view.findViewById<LinearLayout>(R.id.color_buttons).touchables
        buttons.forEach {
            it.setOnClickListener{
                onInputListener!!.sendColor((it as Button).currentHintTextColor)
                dismiss()
            }
        }
    }

    interface OnInputListener {
        fun sendColor(color: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)!!
                .childFragmentManager.fragments[0]
        onInputListener = fragment as OnInputListener


    }
}