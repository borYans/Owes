package com.example.owes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.data.model.entities.Debtor
import com.example.owes.utils.DebtorOnClickListener
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_payments.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Payments : Fragment(R.layout.fragment_payments), DebtorOnClickListener {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter(this)
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapter()
        initializeAdMobAds()


        listenToAddNewPaymentClick()
        showAllPayments()
        showTotalMoney()

    }

    private fun checkPaymentsList(payments: List<Debtor>) {
        if (payments.isEmpty()) {
            noPaymentsInfoTxt.visibility = View.VISIBLE
        } else {
            noPaymentsInfoTxt.visibility = View.GONE
            paymentsRecyclerAdapter.differ.submitList(payments)
        }
    }

    private fun showTotalMoney() {
        debtorViewModel.calculateTotal().observe(viewLifecycleOwner, { money ->
            money?.let {
                if (money.containsKey("POSITIVE_NUMBER")) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_green_light)!!)
                    sumOfMoneyAmount.text = "+$${money.getValue("POSITIVE_NUMBER")}"
                }

                if (money.containsKey("NEGATIVE_NUMBER")) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_red_light)!!)
                    sumOfMoneyAmount.text = "-$${money.getValue("NEGATIVE_NUMBER")}"
                }
            }
        })
    }

    private fun showAllPayments() {
        debtorViewModel.getAllPayments().observe(viewLifecycleOwner, { debtors ->
           debtors?.let {
               it.let {
                   checkPaymentsList(it)
               }
            }
       })
    }

    private fun listenToAddNewPaymentClick() {
        addPaymentButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(PaymentsDirections.actionPaymentsToAddPayment())
        }
    }

    private fun initializeAdMobAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun initializeAdapter() {
        paymentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentsRecyclerAdapter
        }
    }

    private fun askPaidConfirmation(debtor: Debtor) {
        Snackbar.make(requireView(), "Added to paid.", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                debtor.isPayed = false
                debtorViewModel.updateDebtor(debtor)
                showTotalMoney()
            }
        }
            .show()
    }

    override fun onDebtorClick(debtor_name: String) {
        Navigation.findNavController(requireView()).navigate(PaymentsDirections.actionPaymentsToDebtorDetail(debtor_name))
    }
}