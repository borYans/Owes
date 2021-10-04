package com.example.owes.ui

import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.adapters.PartialPaymentRecyclerAdapter
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import com.example.owes.utils.DateConverter.convertDateToSimpleFormatString
import com.example.owes.utils.OwesSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
import com.example.owes.viewmodels.DebtorViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_debtor_detail.*
import java.util.*


class DebtorDetail : Fragment(R.layout.fragment_debtor_detail) {

    private val debtorViewModel: DebtorViewModel by activityViewModels()
    private val partialPaymentRecyclerAdapter = PartialPaymentRecyclerAdapter()

    private var debtorId: Int? = null
    private lateinit var debtor: Debtor
    private var isPaidBtnClicked = false

    //screenshot of the money variables
    private var totalPaidMoney: Double = 0.0
    private var remainingMoney: Double = 0.0


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
                    updateDebtor(String.format("%.2f", partialPaymentRecyclerAdapter.partialPDiffer.currentList[position].amount).toDouble())
                    askDeleteConfirmation(payment)
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelpeCallback)
        itemTouchHelper.attachToRecyclerView(partialPaymentRecycler)

    }

    private fun updateDebtor(partialAmount: Double) {
        debtor.apply {
            val total = totalAmountMoney - partialAmount
            val remaining = remainingAmountMoney + partialAmount
            if (total >= 0.0 && remaining >= 0.0) {
                totalAmountMoney -= partialAmount
                remainingAmountMoney += partialAmount
            }
        }
        debtorViewModel.updateDebtor(debtor)
    }

    private fun askDeleteConfirmation(ppayment: PartialPayment) {
        Snackbar.make(requireView(), "Payment deleted.", Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
            setTextColor(resources.getColor(R.color.white))
            setActionTextColor(resources.getColor(R.color.white))
            setAction("Undo") {
                debtorViewModel.addPartialPayment(ppayment)

                debtor.apply {
                    remainingAmountMoney -= ppayment.amount
                    totalAmountMoney += ppayment.amount
                }
                debtorViewModel.updateDebtor(debtor)
            }
        }
            .show()
    }

    private fun populatePartialPaymentList() {
        debtorId?.let {
            debtorViewModel.getPartialPaymentsForDebtor(debtorId!!).observe(viewLifecycleOwner, {
                it?.let {
                    partialPaymentRecyclerAdapter.partialPDiffer.submitList(it)
                }
            })
        }

    }

    private fun listenToSaveBtn() {
        saveButtonDetail.setOnClickListener {
            debtor.isPayed = isPaidBtnClicked
            if (debtor.isPayed) {
                debtor.apply {
                    totalAmountMoney = totalPaidMoney
                    remainingAmountMoney = remainingMoney
                    paymentDate = convertDateToSimpleFormatString(Calendar.getInstance().time)
                }
                debtorViewModel.updateDebtor(debtor)
            }

            Navigation.findNavController(requireView())
                .navigate(DebtorDetailDirections.actionDebtorDetailToPaidDebts())
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
        markAsPaidBtn.text = getString(R.string.mark_as_paid)
        partialPaymentBtn.visibility = View.VISIBLE
    }

    private fun retrieveRemainingAmount() {
        remainingMoney = debtor.remainingAmountMoney
        totalPaidMoney = debtor.totalAmountMoney
        remainingMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${remainingMoney}"
        totalMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}$totalPaidMoney"
    }

    private fun listenToMarkAsPaidBtn() {
        markAsPaidBtn.setOnClickListener {
            if (isPaidBtnClicked) {
                setUnpaidButtonState()
                retrieveRemainingAmount()
                isPaidBtnClicked = false
                saveButtonDetail.apply {
                    isEnabled = false
                    setBackgroundColor(resources.getColor(R.color.unavailable_save_button_color))
                }
            } else {
                isPaidBtnClicked = true
                clearRemainingAmount()
                setPaidButtonState()
                markAsPaidBtn.text = "Paid on ${convertDateToSimpleFormatString(Calendar.getInstance().time)} | Unpaid ->"
                saveButtonDetail.apply {
                    isEnabled = true
                    setBackgroundColor(resources.getColor(R.color.main_background_color))
                }
            }
        }
    }

    private fun clearRemainingAmount() {
        totalPaidMoney = debtor.totalAmountMoney + debtor.remainingAmountMoney
        remainingMoney = 0.0
        remainingMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${String.format("%.2f", remainingMoney)}"
        totalMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${String.format("%.2f", totalPaidMoney)}"
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
            debtorId = args.debtorId
        }
    }

    private fun listenToPartialPaymentsBtn() {
        partialPaymentBtn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(DebtorDetailDirections.actionDebtorDetailToPartialPayments(debtorId!!))
        }
    }


    private fun populateDetailScreen() {
        populatePartialPaymentList()

        debtorViewModel.getOneDebtor(debtorId!!).observe(viewLifecycleOwner, {
            it?.let {
                debtor = it
                totalPaidMoney = it.totalAmountMoney
                remainingMoney = it.remainingAmountMoney
                listenForZeroRemainingMoney()
                checkPaidState()
                setPaymentImageResource()
                debtorNameTxtDetail.text = it.personName
                referenceDetailTxt.text = it.reference
                totalMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${String.format("%.2f", debtor.totalAmountMoney).toDouble()}"
                remainingMoneyDetail.text = "${OwesSharedPrefs.readFromPrefs(getString(R.string.CURRENCY), getString(R.string.DOLLAR))}${String.format("%.2f", debtor.remainingAmountMoney).toDouble()}"
            }
        })
    }

    private fun listenForZeroRemainingMoney() {
        if (remainingMoney == 0.0) {
            isPaidBtnClicked = true
            setPaidButtonState()
            markAsPaidBtn.text = "Paid on ${convertDateToSimpleFormatString(Calendar.getInstance().time)} | Unpaid ->"
            saveButtonDetail.apply {
                isEnabled = true
                setBackgroundColor(resources.getColor(R.color.main_background_color))
            }
        } else {
            setUnpaidButtonState()
            markAsPaidBtn.text = "Mark as paid"
            saveButtonDetail.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.unavailable_save_button_color))
            }
        }
    }

    private fun setPaymentImageResource() {
        if (debtor.isOwned) {
            owesSymbol.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_circle_right_24))
        } else {
            owesSymbol.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_circle_left_24))
        }
    }

    private fun setUpRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        partialPaymentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = partialPaymentRecyclerAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }


}