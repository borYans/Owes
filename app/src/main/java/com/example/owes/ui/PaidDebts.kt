package com.example.owes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.adapters.PaymentsRecyclerAdapter
import com.example.owes.data.model.entities.Debtor
import com.example.owes.utils.DebtorOnClickListener
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_paid_debts.*


class PaidDebts : Fragment(R.layout.fragment_paid_debts), DebtorOnClickListener {

    private val paymentsRecyclerAdapter = PaymentsRecyclerAdapter(this)
    private val debtorViewModel: DebtorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalizeAdapter()
        showAllPaidDebts()

        val itemTouchHelpeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                val debtor = paymentsRecyclerAdapter.differ.currentList[position]
                debtorViewModel.deletePayment(debtor)
                askDeleteConfirmation(debtor)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelpeCallback)
        itemTouchHelper.attachToRecyclerView(paidRecyclerView)

    }

    private fun showAllPaidDebts() {
        debtorViewModel.getAllPaidDebts().observe(viewLifecycleOwner, { paidDebts ->
            paidDebts?.let {
                checkPaidDebts(it)
            }
        })
    }

    private fun checkPaidDebts(paidDebts:List<Debtor>) {
        if(paidDebts.isEmpty()) {
            noPaidDebtsTxt.visibility = View.VISIBLE
        } else {
            noPaidDebtsTxt.visibility = View.GONE
            paymentsRecyclerAdapter.differ.submitList(paidDebts)
        }
    }

    private fun initalizeAdapter() {
        paidRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentsRecyclerAdapter
        }
    }

    private fun askDeleteConfirmation(debtor: Debtor) {
        Snackbar.make(requireView(), "Payment deleted.", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                debtorViewModel.addDebtor(debtor)
            }
        }
            .show()
    }

    override fun onDebtorClick(debtor_name: String) {
        Navigation.findNavController(requireView()).navigate(PaidDebtsDirections.actionPaidDebtsToDebtorDetail(debtor_name))
    }

}