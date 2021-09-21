package com.example.owes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.adapters.PartialPaymentRecyclerAdapter
import com.example.owes.viewmodels.DebtorViewModel
import kotlinx.android.synthetic.main.fragment_debtor_detail.*


class DebtorDetail : Fragment(R.layout.fragment_debtor_detail) {

    private val debtorViewModel: DebtorViewModel by activityViewModels()
    private val partialPaymentRecyclerAdapter = PartialPaymentRecyclerAdapter()

    private var debtorName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        populateDetailScreen()
        listenToPartialPaymentsBtn()

    }

    private fun listenToPartialPaymentsBtn() {
        partialPaymentBtn.setOnClickListener {
            //navigate to another screen.
        }
    }


    private fun populateDetailScreen() {
       // val debtor = debtorViewModel.getOneDebtor(debtorName!!) // null pointer, bad querry!!!
    }

    private fun setUpRecyclerView() {
        partialPaymentRecycler.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = partialPaymentRecyclerAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        arguments?.let {
            val args = DebtorDetailArgs.fromBundle(it)
            debtorName = args.debtorId
        }
    }
}