package com.example.owes.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.model.Debtor

class PartialPaymentRecyclerAdapter: RecyclerView.Adapter<PartialPaymentRecyclerAdapter.PartialPaymentViewHolder>() {


    private val differCallback = object: DiffUtil.ItemCallback<Debtor>() {
        override fun areItemsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
            return oldItem.id === newItem.id
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

        }
    }

    override fun getItemCount()= partialPDiffer.currentList.size
}