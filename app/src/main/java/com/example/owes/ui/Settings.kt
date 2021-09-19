package com.example.owes.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.owes.R
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropDownCurrencyAdapter()

    }

    private fun setDropDownCurrencyAdapter() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_currency_item, resources.getStringArray(R.array.currency_array))
        currencyDropdown.setAdapter(arrayAdapter)
    }
}