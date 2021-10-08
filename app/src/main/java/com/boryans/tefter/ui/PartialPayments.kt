package com.boryans.tefter.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.boryans.tefter.R
import com.boryans.tefter.data.model.entities.Debtor
import com.boryans.tefter.data.model.entities.PartialPayment
import com.boryans.tefter.utils.DateConverter.convertDateToSimpleFormatString
import com.boryans.tefter.utils.classicSnackBar
import com.boryans.tefter.viewmodels.DebtorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_add_payment.*
import kotlinx.android.synthetic.main.fragment_partial_payments.*
import java.util.*


class PartialPayments : Fragment(R.layout.fragment_partial_payments) {

    var debtorId: Int? = null
    lateinit var debtor: Debtor
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdMobAds()
        getDebtorNameFromArgs()
        getDebtorObject()

        savePartialBtn.setOnClickListener {
            if(partialAmountMoneyInput.text.isNullOrEmpty() || partialAmountMoneyInput.text.toString().toDouble() == 0.0){
                requireView().classicSnackBar("Money amount is mandatory.")
            } else {
                val pPayment = PartialPayment(
                    null,
                    convertDateToSimpleFormatString(Calendar.getInstance().time),
                    partialAmountMoneyInput.text.toString().toDouble(),
                    debtorId!!
                )

                if(debtor.remainingAmountMoney < partialAmountMoneyInput.text.toString().toDouble()) {
                    requireView().classicSnackBar("You have exceeded the limit of the remaining amount.")
                } else {
                    debtor.remainingAmountMoney -= partialAmountMoneyInput.text.toString().toDouble()
                    debtor.totalAmountMoney += partialAmountMoneyInput.text.toString().toDouble()
                    debtorViewModel.addPartialPayment(pPayment)
                    debtorViewModel.updateDebtor(debtor)
                    Navigation.findNavController(requireView()).navigate(PartialPaymentsDirections.actionPartialPaymentsToDebtorDetail(debtorId!!))
                }
            }


        }
    }

    private fun initializeAdMobAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        adViewPartialPayments.loadAd(adRequest)
    }

    private fun getDebtorObject() {
        debtorViewModel.getOneDebtor(debtorId!!).observe(viewLifecycleOwner, { deb ->
            deb?.let {
                debtor = it
            }
        })
    }

    private fun getDebtorNameFromArgs() {
        arguments?.let {
            val args = PartialPaymentsArgs.fromBundle(it)
            debtorId = args.debtorId
        }
    }
}