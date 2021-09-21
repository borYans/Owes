package com.example.owes.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.owes.R
import com.example.owes.data.model.Debtor
import com.example.owes.viewmodels.DebtorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_payment.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddPayment : Fragment(R.layout.fragment_add_payment) {

    private var dueDate: String? = null

    private val debtorViewModel: DebtorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitalCheckBoxesState()
        handleCheckBoxClicks()
        listenForCalendarInput()


        listenToSaveButton()
    }

    private fun setInitalCheckBoxesState() {
        owedCheckBox.isChecked = false
        oweCheckBox.isChecked = true

    }

    private fun handleCheckBoxClicks() {
        oweCheckBox.setOnClickListener {
            owedCheckBox.isChecked = !oweCheckBox.isChecked
        }
        owedCheckBox.setOnClickListener {
            oweCheckBox.isChecked = !owedCheckBox.isChecked
        }
    }

    private fun listenToSaveButton() {
        saveButton.setOnClickListener {
            //need to validate first, this is test purpose
            val debtor = Debtor(
                null,
                oweCheckBox.isChecked,
                nameInputBox.text.toString(),
                amountInputBox.text.toString().toInt(),
                referenceInputBox.text.toString(),
                dueDate.toString(),
                false                //for adding payment this is set to false as default.
            )
                debtorViewModel.addDebtor(debtor)

           Navigation.findNavController(requireView()).navigate(AddPaymentDirections.actionAddPaymentToPayments())
        }
    }


    private fun listenForCalendarInput() {
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, YEAR, MONTH, DAY ->
            val calendar = Calendar.getInstance()
            calendar.set(YEAR, MONTH, DAY)
            dueDate = formatDate(calendar.time)
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

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}