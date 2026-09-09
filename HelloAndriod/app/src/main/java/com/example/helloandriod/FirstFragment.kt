package com.example.helloandriod

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class FirstFragment : Fragment(R.layout.fragment_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Binding all the buttons that we have created
        val buttonOne = view.findViewById<Button>(R.id.button_first)
        val buttonTwo = view.findViewById<Button>(R.id.button_second)
        val buttonThree = view.findViewById<Button>(R.id.button_third)
        val buttonFour = view.findViewById<Button>(R.id.button_fourth)
        val buttonFive = view.findViewById<Button>(R.id.button_fifth)

        // Opens second fragment when the buttons are clicked and passes the text

        buttonOne.setOnClickListener {
            openSecondFragment(buttonOne.text.toString())
        }

        buttonTwo.setOnClickListener {
            openSecondFragment(buttonTwo.text.toString())
        }

        buttonThree.setOnClickListener {
            openSecondFragment(buttonThree.text.toString())
        }

        buttonFour.setOnClickListener {
            openSecondFragment(buttonFour.text.toString())
        }

        buttonFive.setOnClickListener {
            openSecondFragment(buttonFive.text.toString())
        }
    }

    // This passes the button's text to SecondFragment and navigates to it
    private fun openSecondFragment(buttonText: String) {
        val bundle = Bundle()
        bundle.putString("button_text", buttonText)

        val secondFragment = SecondFragment()
        secondFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }
}