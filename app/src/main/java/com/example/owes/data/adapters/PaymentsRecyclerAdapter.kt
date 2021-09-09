package com.example.owes.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.db.Debtor
import kotlinx.android.synthetic.main.item_payment.view.*

class PaymentsRecyclerAdapter : RecyclerView.Adapter<PaymentsRecyclerAdapter.PaymentsViewHolder>() {


    inner class PaymentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Debtor>() {
        override fun areItemsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
           return oldItem == newItem
        }
    }

     val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        val debtor = differ.currentList[position]

        holder.itemView.apply {
            when(debtor.isOwned) {
                true -> {
                    imageView.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.money_inside_flow_24)) //check this solution again.
                }
                else -> {
                    imageView.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.ic_money_outside_flow_24)) //check this solution again.
                    amountMoney.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_red_light))
                }
            }
            debtorName.text = debtor.personName
            amountMoney.text = debtor.amountMoney.toString()
            dueDateText.text = debtor.dueDate


        }
    }

    override fun getItemCount() = differ.currentList.size
}