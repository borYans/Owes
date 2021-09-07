package com.example.owes.ui

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_payments.*
import androidx.lifecycle.ViewModelProvider


class Payments : Fragment(R.layout.fragment_payments) {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter()
   // private lateinit var debtorViewModel: DebtorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //debtorViewModel = ViewModelProvider(requireActivity()).get(DebtorViewModel::class.java)

        initializeAdapter()
        initializeAdMobAds()
        listenToAddNewPaymentClick()

    }

  //  private fun showAllPayments() {
     //   debtorViewModel.getAllPayments().observe(viewLifecycleOwner, { debtors ->
      //      debtors?.let {
     //           it.let {
          //          paymentsRecyclerAdapter.differ.submitList(it)
          //      }
        //    }

    //    })
  //  }

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