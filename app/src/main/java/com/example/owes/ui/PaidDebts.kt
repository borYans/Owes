package com.example.owes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_paid_debts.*


class PaidDebts : Fragment(R.layout.fragment_paid_debts) {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter()
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalizeAdapter()
        showAllPaidDebts()

    }

    private fun showAllPaidDebts() {
        debtorViewModel.getAllPaidDebts().observe(viewLifecycleOwner, { paidDebts ->
            paidDebts?.let {
                paymentsRecyclerAdapter.differ.submitList(it)
            }
        })

    }

    private fun initalizeAdapter() {
        paidRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentsRecyclerAdapter
        }
    }

}