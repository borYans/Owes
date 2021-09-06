package com.example.owes.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.owes.R
import com.example.owes.data.db.Debtor
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.fragment_add_payment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AddPayment : Fragment(R.layout.fragment_add_payment) {

    private var dueDate: String? = null

    private lateinit var debtorViewModel: DebtorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        debtorViewModel = (activity as MainActivity).debtorViewModel
        setInitalCheckBoxesState()
        handleCheckBoxClicks()
        listenForCalendarInput()

        listenToSaveButton()

    }

    private fun listenToSaveButton() {
        saveButton.setOnClickListener {
            //need to validate first, this is test purpose
            val debtor = Debtor(
                null,
                nameInputBox.text.toString(),
                amountInputBox.text.toString().toInt(),
                referenceInputBox.text.toString(),
                reccuringPaymentCheck.isChecked,
                dueDate.toString()
            )
                debtorViewModel.addDebtor(debtor)

        }

    }


    private fun setInitalCheckBoxesState() {
        owedCheckBox.isChecked = false
        oweCheckBox.isChecked = true

        oneOffCheckButton.isChecked = true
        reccuringPaymentCheck.isChecked = false
    }

    private fun handleCheckBoxClicks() {
        oweCheckBox.setOnClickListener {
            owedCheckBox.isChecked = !oweCheckBox.isChecked
        }
        owedCheckBox.setOnClickListener {
            oweCheckBox.isChecked = !owedCheckBox.isChecked
        }

        oneOffCheckButton.setOnClickListener {
            reccuringPaymentCheck.isChecked = !oneOffCheckButton.isChecked
            showDueDateButton()
        }

        reccuringPaymentCheck.setOnClickListener {
            oneOffCheckButton.isChecked = !reccuringPaymentCheck.isChecked
            showCreateScheduleButton()
        }

    }

    private fun showCreateScheduleButton() {
        createSchedule.visibility = View.VISIBLE
        dueDateButton.visibility = View.GONE
    }

    private fun showDueDateButton() {
        dueDateButton.visibility = View.VISIBLE
        createSchedule.visibility = View.GONE

    }

    private fun listenForCalendarInput() {
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, YEAR, MONTH, DAY ->
            val calendar = Calendar.getInstance()
            calendar.set(YEAR, MONTH, DAY)
            dueDate = calendar.time.toString()
            dueDateButton.text = dueDate  //you can format it a little bit better later.
        }

        dueDateButton.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                datePicker,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}