package com.example.helloandriod

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment(R.layout.fragment_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This gets the text that was passed from the first fragment
        val selectedButtonText =
            arguments?.getString("button_text") ?: ""
        // This displays the text in the text view
        val textView =
            view.findViewById<TextView>(R.id.selected_button_text)

        textView.text = selectedButtonText
    }
}