package com.example.owes.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.owes.R
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.utils.DateConverter.convertDateToSimpleFormatString
import com.example.owes.utils.classicSnackBar
import com.example.owes.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_partial_payments.*
import java.util.*


class PartialPayments : Fragment(R.layout.fragment_partial_payments) {

    var debtorName: String? = null
    lateinit var debtor: Debtor
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDebtorNameFromArgs()
        getDebtorObject()

        savePartialBtn.setOnClickListener {
            val pPayment = PartialPayment(
                null,
                convertDateToSimpleFormatString(Calendar.getInstance().time),
                partialAmountMoneyInput.text.toString().toInt(),
                debtorName!!
            )

            if(debtor.remainingAmountMoney < partialAmountMoneyInput.text.toString().toInt()) {
                requireView().classicSnackBar("You have exceeded the limit of the remaining amount.")
            } else {
                debtor.remainingAmountMoney -= partialAmountMoneyInput.text.toString().toInt()
                debtor.totalAmountMoney += partialAmountMoneyInput.text.toString().toInt()
                debtorViewModel.addPartialPayment(pPayment)
                debtorViewModel.updateDebtor(debtor)
                Navigation.findNavController(requireView()).navigate(PartialPaymentsDirections.actionPartialPaymentsToDebtorDetail(debtorName!!))
            }
        }

    }

    private fun getDebtorObject() {
        debtorViewModel.getOneDebtor(debtorName!!).observe(viewLifecycleOwner, { deb ->
            deb?.let {
                debtor = it
            }
        })
    }

    private fun getDebtorNameFromArgs() {
        arguments?.let {
            val args = PartialPaymentsArgs.fromBundle(it)
            debtorName = args.debtorName
        }
    }
}