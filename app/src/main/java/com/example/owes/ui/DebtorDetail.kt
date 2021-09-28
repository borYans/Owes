package com.example.owes.ui

import android.graphics.Color.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.adapters.PartialPaymentRecyclerAdapter
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.data.model.relations.DebtorWithPPayments
import com.example.owes.utils.DateConverter.convertDateToSimpleFormatString
import com.example.owes.utils.toast
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_debtor_detail.*
import kotlinx.android.synthetic.main.fragment_paid_debts.*
import java.util.*
import kotlin.collections.ArrayList


class DebtorDetail : Fragment(R.layout.fragment_debtor_detail) {

    private val debtorViewModel: DebtorViewModel by activityViewModels()
    private val partialPaymentRecyclerAdapter = PartialPaymentRecyclerAdapter()

    private var debtorName: String? = null
    private lateinit var debtor: Debtor
    private var isPaidBtnClicked = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        getNameFromArgs()

        populateDetailScreen()
        populatePartialPaymentList()
        listenToPartialPaymentsBtn()
        listenToMarkAsPaidBtn()
        listenToSaveBtn()


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
                val payment = partialPaymentRecyclerAdapter.partialPDiffer.currentList[position]
                debtorViewModel.deletePPayment(payment)
                askDeleteConfirmation(payment)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelpeCallback)
        itemTouchHelper.attachToRecyclerView(partialPaymentRecycler)

    }

    private fun askDeleteConfirmation(ppayment: PartialPayment) {
        Snackbar.make(requireView(), "Payment deleted.", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                debtorViewModel.addPartialPayment(partialPayment = ppayment)
            }
        }
            .show()
    }

    private fun populatePartialPaymentList() {
        debtorName?.let {
            debtorViewModel.getPartialPaymentsForDebtor(debtorName!!).observe(viewLifecycleOwner, {
                it?.let {
                    partialPaymentRecyclerAdapter.partialPDiffer.submitList(it)
                }
            })
        }
    }

    private fun listenToSaveBtn() {
        saveButtonDetail.setOnClickListener {
            debtor.isPayed = isPaidBtnClicked
            debtor.paymentDate = convertDateToSimpleFormatString(Calendar.getInstance().time)
            debtorViewModel.updateDebtor(debtor)
        }
    }

    private fun checkPaidState() {
        if (debtor.isPayed) {
            clearRemainingAmount()
            setPaidButtonState()
            markAsPaidBtn.text = "Paid on ${debtor.paymentDate}"
        } else {
            retrieveRemainingAmount()
        }
    }

    private fun setUnpaidButtonState() {
        markAsPaidBtn.setBackgroundColor(resources.getColor(android.R.color.transparent))
        markAsPaidBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_confirm_24, 0)
        markAsPaidBtn.setTextColor(BLACK)
        markAsPaidBtn.text = "Mark as paid"
        partialPaymentBtn.visibility = View.VISIBLE
    }

    private fun retrieveRemainingAmount() {
        debtor.remainingAmountMoney = debtor.totalAmountMoney
        remainingMoneyDetail.text = "$${debtor.remainingAmountMoney}"
    }

    private fun listenToMarkAsPaidBtn() {
        markAsPaidBtn.setOnClickListener {
           if (isPaidBtnClicked) {
               setUnpaidButtonState()
               retrieveRemainingAmount()
               isPaidBtnClicked = false
           } else {
               isPaidBtnClicked = true
               clearRemainingAmount()
               setPaidButtonState()
               markAsPaidBtn.text = "Paid on ${convertDateToSimpleFormatString(Calendar.getInstance().time)} | Unpaid ->"
           }
        }
    }

    private fun clearRemainingAmount() {
        debtor.remainingAmountMoney = 0
        remainingMoneyDetail.text = "$0"
    }

    private fun setPaidButtonState() {
        markAsPaidBtn.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
        markAsPaidBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        markAsPaidBtn.setTextColor(WHITE)
        partialPaymentBtn.visibility = View.GONE
    }

    private fun getNameFromArgs() {
        arguments?.let {
            val args = DebtorDetailArgs.fromBundle(it)
            debtorName = args.debtorId
        }
    }

    private fun listenToPartialPaymentsBtn() {
        partialPaymentBtn.setOnClickListener {
            //navigate to another screen.
            Navigation.findNavController(requireView()).navigate(DebtorDetailDirections.actionDebtorDetailToPartialPayments(debtorName!!))
        }
    }


    private fun populateDetailScreen() {
        debtorViewModel.getOneDebtor(debtorName!!).observe(viewLifecycleOwner, {
            it?.let {
                debtor = it
                checkPaidState()
                debtorNameTxtDetail.text = it.personName
                remainingMoneyDetail.text = "$${it.remainingAmountMoney}"
                totalMoneyDetail.text = "$${it.totalAmountMoney}"
                referenceDetailTxt.text = it.reference
            }
        })
    }

    private fun setUpRecyclerView() {
        partialPaymentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = partialPaymentRecyclerAdapter
        }
    }


}