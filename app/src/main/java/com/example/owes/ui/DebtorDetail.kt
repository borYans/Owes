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
import com.example.owes.utils.OwesSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
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

    //screenshot of the money variables
    private var totalPaidMoney: Int = 0
    private var remainingMoney: Int = 0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs(requireContext())
        setUpRecyclerView()
        getNameFromArgs()


        populateDetailScreen()
        listenToPartialPaymentsBtn()
        listenToMarkAsPaidBtn()
        listenToSaveBtn()



        val itemTouchHelpeCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val payment = partialPaymentRecyclerAdapter.partialPDiffer.currentList[position]
                    debtorViewModel.deletePPayment(payment)
                    updateDebtor(partialPaymentRecyclerAdapter.partialPDiffer.currentList[position].amount)
                    askDeleteConfirmation(payment)
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelpeCallback)
        itemTouchHelper.attachToRecyclerView(partialPaymentRecycler)

    }


    private fun updateDebtor(partialAmount: Int) {
        debtor.apply {
            totalAmountMoney -= partialAmount
            remainingAmountMoney += partialAmount
        }
        debtorViewModel.updateDebtor(debtor)
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
            if(debtor.isPayed) {
                debtor.apply {
                    totalAmountMoney = totalPaidMoney
                    remainingAmountMoney = remainingMoney
                    paymentDate = convertDateToSimpleFormatString(Calendar.getInstance().time)
                }
                debtorViewModel.updateDebtor(debtor)
            }

            Navigation.findNavController(requireView()).navigate(DebtorDetailDirections.actionDebtorDetailToPaidDebts())
        }
    }

    private fun checkPaidState() {
        if (debtor.isPayed) {
            clearRemainingAmount()
            setPaidButtonState()
            setMarkAsPaidButtonState()
            saveButtonDetail.visibility = View.GONE
        } else {
            retrieveRemainingAmount()
        }
    }

    private fun setMarkAsPaidButtonState() {
        markAsPaidBtn.text = "Paid on ${debtor.paymentDate}"
        markAsPaidBtn.isClickable = false
        markAsPaidBtn.setBackgroundColor(resources.getColor(R.color.not_available_button))
    }

    private fun setUnpaidButtonState() {
        markAsPaidBtn.setBackgroundColor(resources.getColor(android.R.color.transparent))
        markAsPaidBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_confirm_24,
            0
        )
        markAsPaidBtn.setTextColor(BLACK)
        markAsPaidBtn.text = "Mark as paid"
        partialPaymentBtn.visibility = View.VISIBLE
    }

    private fun retrieveRemainingAmount() {
        remainingMoney = debtor.remainingAmountMoney
        totalPaidMoney = debtor.totalAmountMoney
        remainingMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs("string", "")}${remainingMoney}"
        totalMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs("string", "")}$totalPaidMoney"
    }

    private fun listenToMarkAsPaidBtn() {
        markAsPaidBtn.setOnClickListener {
            if (isPaidBtnClicked) {
                setUnpaidButtonState()
                retrieveRemainingAmount()
                partialPaymentRecycler.visibility = View.VISIBLE
                isPaidBtnClicked = false
                saveButtonDetail.apply {
                    isEnabled = false
                    setBackgroundColor(resources.getColor(R.color.unavailable_save_button_color))
                }
            } else {
                isPaidBtnClicked = true
                clearRemainingAmount()
                setPaidButtonState()
                partialPaymentRecycler.visibility = View.GONE
                markAsPaidBtn.text =
                    "Paid on ${convertDateToSimpleFormatString(Calendar.getInstance().time)} | Unpaid ->"
                saveButtonDetail.apply {
                    isEnabled = true
                    setBackgroundColor(resources.getColor(R.color.main_background_color))
                }
            }
        }
    }

    private fun clearRemainingAmount() {
        totalPaidMoney = debtor.totalAmountMoney + debtor.remainingAmountMoney
        remainingMoney = 0
        remainingMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs("string", "")}$remainingMoney"
        totalMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs("string", "")}${totalPaidMoney}"
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
            Navigation.findNavController(requireView())
                .navigate(DebtorDetailDirections.actionDebtorDetailToPartialPayments(debtorName!!))
        }
    }


    private fun populateDetailScreen() {
        populatePartialPaymentList()

        debtorViewModel.getOneDebtor(debtorName!!).observe(viewLifecycleOwner, {
            it?.let {
                debtor = it
                totalPaidMoney = it.totalAmountMoney
                remainingMoney = it.remainingAmountMoney
                checkPaidState()
                setPaymentImageResource()
                debtorNameTxtDetail.text = it.personName
                referenceDetailTxt.text = it.reference
                totalMoneyDetail.text =
                    "${OwesSharedPrefs.readFromPrefs("string", "")}${debtor.totalAmountMoney}"
                remainingMoneyDetail.text =
                    "${OwesSharedPrefs.readFromPrefs("string", "")}${debtor.remainingAmountMoney}"
            }
        })
    }

    private fun setPaymentImageResource() {
        if (debtor.isOwned) {
            owesSymbol.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_circle_right_24))
        } else {
            owesSymbol.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_circle_left_24))
        }
    }

    private fun setUpRecyclerView() {
        partialPaymentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = partialPaymentRecyclerAdapter
        }
    }


}