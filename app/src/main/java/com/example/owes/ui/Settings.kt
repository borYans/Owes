package com.example.owes.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.owes.R
import com.example.owes.worker.NotificationWorker
import com.example.owes.utils.Constants.NOTIFICATION_STATUS
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.readBooleanFromPrefs
import com.example.owes.utils.OwesSharedPrefs.readFromPrefs
import com.example.owes.utils.OwesSharedPrefs.saveBooleanToSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.saveStringToSharedPrefs
import com.example.owes.utils.classicSnackBar
import com.example.owes.utils.confirmationSnackBar
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.concurrent.TimeUnit

class Settings : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs(requireContext())
        setUpCurrencyDropDownText()
        setDropDownCurrencyAdapter()
        restoreNotificationToggle()

        listenSaveButton()
        listenRemoveAdsButton()
    }

    private fun setUpCurrencyDropDownText() {
        val currency = readFromPrefs(resources.getString(R.string.CURRENCY), resources.getString(R.string.DOLLAR))
        currencyDropdown.setText(currency)
    }

    private fun restoreNotificationToggle() {
        readBooleanFromPrefs(NOTIFICATION_STATUS, true)?.let {
            notificationTogle.isChecked = it
        }
    }
    private fun setNotificationWorker() {
            if (notificationTogle.isChecked) {
                 val showNotification = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS).build()
                    WorkManager.getInstance(requireContext()).enqueue(showNotification)
            } else {
                WorkManager.getInstance(requireContext()).cancelAllWork()
            }
    }

    private fun listenRemoveAdsButton() {
        removeAdsBtn.setOnClickListener {
            // flavors
        }
    }

    private fun listenSaveButton() {
        saveSettingsBtn.setOnClickListener {
            setNotificationWorker()
            saveBooleanToSharedPrefs(NOTIFICATION_STATUS,notificationTogle.isChecked)
            saveStringToSharedPrefs(getString(R.string.CURRENCY),currencyInputLayout.editText?.text.toString())
            Log.d("SETTINGS", "SHARED PREFERENCES: ${readFromPrefs("string", "")}")
            requireView().confirmationSnackBar("Done.")
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