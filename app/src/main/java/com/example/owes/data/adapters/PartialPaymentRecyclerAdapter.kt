package com.example.owes.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.model.entities.Debtor
import com.example.owes.data.model.entities.PartialPayment
import kotlinx.android.synthetic.main.item_partial_payment.view.*

class PartialPaymentRecyclerAdapter: RecyclerView.Adapter<PartialPaymentRecyclerAdapter.PartialPaymentViewHolder>() {


    private val differCallback = object: DiffUtil.ItemCallback<PartialPayment>() {
        override fun areItemsTheSame(oldItem: PartialPayment, newItem: PartialPayment): Boolean {
            return oldItem.paymentId == newItem.paymentId
        }

        override fun areContentsTheSame(oldItem: PartialPayment, newItem: PartialPayment): Boolean {
            return oldItem.paymentId === newItem.paymentId
        }
    }

    val partialPDiffer = AsyncListDiffer(this, differCallback)

    inner class PartialPaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialPaymentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_partial_payment, parent, false)
        return PartialPaymentViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: PartialPaymentViewHolder, position: Int) {
        val partialPayment = partialPDiffer.currentList[position]
        holder.itemView.apply {
            datePartialPayment.text = partialPayment.date
            amountPartialPayment.text = partialPayment.amount.toString()
        }
    }

    override fun getItemCount()= partialPDiffer.currentList.size
}