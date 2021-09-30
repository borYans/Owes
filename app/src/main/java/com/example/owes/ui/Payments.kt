package com.example.owes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_payments.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_payment.view.*

@AndroidEntryPoint
class Payments : Fragment(R.layout.fragment_payments) {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter()
    private val debtorViewModel: DebtorViewModel by viewModels()

    private var sumOfIncomeAmount = arrayListOf<Int>()
    var outcomeAmount = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapter()
        initializeAdMobAds()


        listenToAddNewPaymentClick()
        showAllPayments()
        showTotalMoney()

    }

    private fun showTotalMoney() {
        debtorViewModel.calculateTotal().observe(viewLifecycleOwner, { money ->
            money?.let {
                if (money.containsKey("POSITIVE_NUMBER")) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_green_light)!!)
                    sumOfMoneyAmount.text = money.getValue("POSITIVE_NUMBER").toString()
                }

                if (money.containsKey("NEGATIVE_NUMBER")) {
                    sumOfMoneyAmount.setTextColor(context?.resources?.getColor(android.R.color.holo_red_light)!!)
                    sumOfMoneyAmount.text = "- ${money.getValue("NEGATIVE_NUMBER")}"
                }
            }
        })
    }


    private fun showAllPayments() {
        debtorViewModel.getAllPayments().observe(viewLifecycleOwner, { debtors ->
           debtors?.let {
               it.let {
                   paymentsRecyclerAdapter.differ.submitList(it)
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
}