package com.example.owes.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.owes.R
import com.example.owes.utils.Constants.NOTIFICATION_STATUS
import com.example.owes.utils.OwesSharedPrefs.readFromPrefs
import com.example.owes.utils.OwesSharedPrefs.saveBooleanToSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.saveStringToSharedPrefs
import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownCurrencyAdapter()

        listenSaveButton()
        listenRemoveAdsButton()

    }

    private fun listenRemoveAdsButton() {
        removeAdsBtn.setOnClickListener {
            // flavors
        }
    }

    private fun listenSaveButton() {
        saveSettingsBtn.setOnClickListener {
            saveBooleanToSharedPrefs(NOTIFICATION_STATUS,notificationTogle.isChecked)
            saveStringToSharedPrefs(getString(R.string.CURRENCY),currencyInputLayout.editText?.text.toString())
            Log.d("SETTINGS", "SHARED PREFERENCES: ${readFromPrefs("string", "")}")
        }
    }

    private fun setDropDownCurrencyAdapter() {
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_currency_item,
            resources.getStringArray(R.array.currency_array)
        )
        currencyDropdown.setAdapter(arrayAdapter)
    }
}