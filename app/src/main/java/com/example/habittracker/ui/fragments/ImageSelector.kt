package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.image_select_dialog.*

class ImageSelector : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_select_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView: NavigationView? = activity?.findViewById(R.id.nav_view)
        val userImage = navView!!.getHeaderView(0).findViewById<ImageView>(R.id.user_image)
        image_save_button.setOnClickListener {
            val reference = edit_reference.text.toString()
            Glide.with(MainActivity.CONTEXT).load(reference)
                .circleCrop().placeholder(R.mipmap.ic_launcher).into(userImage)
            dismiss()
        }
    }
}