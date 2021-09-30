package com.example.owes.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.owes.R
import com.example.owes.data.model.entities.Debtor
import com.example.owes.utils.DebtorOnClickListener
import com.example.owes.utils.OwesSharedPrefs.initSharedPrefs
import com.example.owes.utils.OwesSharedPrefs.readFromPrefs
import kotlinx.android.synthetic.main.item_payment.view.*

class PaymentsRecyclerAdapter(
    private val debtorOnClickListener: DebtorOnClickListener,
) : RecyclerView.Adapter<PaymentsRecyclerAdapter.PaymentsViewHolder>() {



    inner class PaymentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Debtor>() {
        override fun areItemsTheSame(oldItem: Debtor, newItem: Debtor): Boolean {
            return oldItem.personName == newItem.personName
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
        initSharedPrefs(holder.itemView.context)
        val curr = readFromPrefs(holder.itemView.context.getString(R.string.CURRENCY), holder.itemView.context.getString(R.string.DOLLAR))

        val debtor = differ.currentList[position]

        holder.itemView.apply {
            when(debtor.isOwned) {
                true -> {
                    imageView.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.ic_baseline_arrow_circle_right_24)) //check this solution again.
                    if (debtor.isPayed) amountMoney.text = "+$curr${debtor.totalAmountMoney}" else  amountMoney.text = "+$curr${debtor.remainingAmountMoney}"

                }
                else -> {
                    imageView.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.ic_baseline_arrow_circle_left_24)) //check this solution again.
                    amountMoney.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_red_light))
                    if (debtor.isPayed) amountMoney.text = "-$curr${debtor.totalAmountMoney}" else  amountMoney.text = "-$curr${debtor.remainingAmountMoney}"

                }
            }
            debtorName.text = debtor.personName
            dueDateText.text = "${context.getString(R.string.due_date)}  ${debtor.dueDate}"

            holder.itemView.setOnClickListener{
                debtorOnClickListener.onDebtorClick(debtor.personName)
            }


        }
    }

    override fun getItemCount() = differ.currentList.size
}