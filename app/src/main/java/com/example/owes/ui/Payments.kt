package com.example.owes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import com.example.owes.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_payments.*


class Payments : Fragment(R.layout.fragment_payments) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdMobAds()
        listenToAddPaymentClick()
    }

    private fun listenToAddPaymentClick() {
        addPaymentButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(PaymentsDirections.actionPaymentsToAddPayment())
        }
    }

    private fun initializeAdMobAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}