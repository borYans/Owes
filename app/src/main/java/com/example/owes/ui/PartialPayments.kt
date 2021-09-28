package com.example.owes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.owes.R
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.utils.DateConverter.convertDateToSimpleFormatString
import com.example.owes.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_partial_payments.*
import java.util.*


class PartialPayments : Fragment(R.layout.fragment_partial_payments) {

    var debtorName: String? = null
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDebtorNameFromArgs()

        savePartialBtn.setOnClickListener {
            val pPayment = PartialPayment(
                null,
                convertDateToSimpleFormatString(Calendar.getInstance().time),
                partialAmountMoneyInput.text.toString().toInt(),
                debtorName!!
            )
            debtorViewModel.addPartialPayment(partialPayment = pPayment)
        }


    }

    private fun getDebtorNameFromArgs() {
        arguments?.let {
            val args = PartialPaymentsArgs.fromBundle(it)
            debtorName = args.debtorName
        }
    }
}